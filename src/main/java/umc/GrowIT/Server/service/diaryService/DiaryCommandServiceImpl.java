package umc.GrowIT.Server.service.diaryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.*;
import umc.GrowIT.Server.converter.DiaryConverter;
import umc.GrowIT.Server.converter.FlaskConverter;
import umc.GrowIT.Server.domain.*;
import umc.GrowIT.Server.repository.*;
import umc.GrowIT.Server.util.dto.CreditGrantResult;
import umc.GrowIT.Server.util.CreditUtil;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryRequestDTO;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;
import umc.GrowIT.Server.web.dto.FlaskDTO.FlaskRequestDTO;
import umc.GrowIT.Server.web.dto.FlaskDTO.FlaskResponseDTO;
import umc.GrowIT.Server.web.dto.OpenAIDTO.ChatGPTRequest;
import umc.GrowIT.Server.web.dto.OpenAIDTO.ChatGPTResponse;
import umc.GrowIT.Server.web.dto.OpenAIDTO.Message;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiaryCommandServiceImpl implements DiaryCommandService{
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final ChallengeKeywordRepository challengeKeywordRepository;
    private final KeywordRepository keywordRepository;
    private final ChallengeRepository challengeRepository;

    private final CreditUtil creditUtil;

    @Value("${openai.model1}")
    private String chatModel;

    @Value("${openai.model2}")
    private String summaryModel;

    @Value("${openai.model3}")
    private String fineTunedSummaryModel;

    @Value("${openai.keyword-model}")
    private String keywordModel;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @Autowired
    private RestTemplate subTemplate;

    //userId-대화내용 저장용 HashMap
    private final Map<Long, List<Message>> conversationHistory = new HashMap<>();

    @Override
    public DiaryResponseDTO.ModifyDiaryResultDTO modifyDiary(DiaryRequestDTO.ModifyDiaryDTO request, Long diaryId, Long userId) {

        //유저 조회
        userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Optional<Diary> optionalDiary = diaryRepository.findByUserIdAndId(userId, diaryId);
        Diary diary = optionalDiary.orElseThrow(()->new DiaryHandler(ErrorStatus.DIARY_NOT_FOUND));

        //기존의 내용과 변경되지 않았을 경우
        if(diary.getContent().equals(request.getContent())){
            throw new DiaryHandler(ErrorStatus.DIARY_SAME_CONTENT);
        }

        diary.updateContent(request.getContent());

        diaryRepository.save(diary);
        return DiaryConverter.toModifyResultDTO(diary);
    }

    @Override
    @Transactional
    public DiaryResponseDTO.CreateDiaryResultDTO createDiary(DiaryRequestDTO.CreateDiaryDTO request, Long userId) {

        //유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 일기 날짜 검사
        checkDiaryDate(userId, request.getDate());

        //일기 생성
        Diary diary = Diary.builder()
                .content(request.getContent())
                .user(user)
                .date(request.getDate())
                .build();

        //일기 저장
        diary = diaryRepository.save(diary);

        // 사용자의 크레딧 수 증가
        CreditGrantResult result = creditUtil.grantDiaryCredit(user, diary);

        return DiaryConverter.toCreateResultDTO(diary, result);
    }

    @Override
    public void deleteDiary(Long diaryId, Long userId) {
        //유저 조회
        userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Optional<Diary> optionalDiary = diaryRepository.findByUserIdAndId(userId, diaryId);
        Diary diary = optionalDiary.orElseThrow(()->new DiaryHandler(ErrorStatus.DIARY_NOT_FOUND));

        //일기 삭제
        diaryRepository.delete(diary);
    }

    @Override
    public DiaryResponseDTO.VoiceChatResultDTO chatByVoice(DiaryRequestDTO.VoiceChatDTO request, Long userId) {

        //유저 조회
        userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        String userChat = request.getChat();

        conversationHistory.putIfAbsent(userId, new ArrayList<>());

        // 기존 대화 목록 가져오기
        List<Message> messages = conversationHistory.get(userId);

        // 처음 대화라면 시스템 프롬프트 추가
        if (messages.isEmpty()){
            messages.add(
                    new Message(
                            "system",
                            "너는 사용자의 하루 이야기를 들어주는 따뜻한 대화 파트너야.\n\n" +

                                    "[규칙]\n" +
                                    "사용자가 오늘 하루 있었던 일을 이야기하면\n" +
                                    "- 친구처럼 편안하고 부드러운 대화하기\n" +
                                    "- 짧고 따뜻하게 공감하거나 리액션하기\n" +
                                    "- 꼬리질문이나 대화 유도하지 않기\n" +
                                    "- 오직 사용자가 실제로 말한 표현만 활용하기\n" +

                                    "[출력 형식]\n" +
                                    "- 1~2문장으로 답변하기\n" +
                                    "- 사용자가 아무 말도 안 하면, 작은 일이라도 떠올리도록 가볍게 유도하기\n" +
                                    "- 한국어로 자연스럽게 대화하기"
                    )
            );
        }

        // 사용자의 입력을 대화 목록에 추가
        messages.add(new Message("user", userChat));

//        // ChatGPT 요청 생성
//        ChatGPTRequest gptRequest = new ChatGPTRequest(chatModel, messages);
//
//        // API 요청 및 응답 처리
//        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, gptRequest, ChatGPTResponse.class);
//
//        if (chatGPTResponse.getChoices().isEmpty()) {
//            throw new OpenAIHandler(ErrorStatus.GPT_RESPONSE_EMPTY);
//        }

        String aiChat = requestGptChat(chatModel, messages);

        // ai의 답변을 대화 목록에 추가
        messages.add(new Message("assistant", aiChat));

        // 대화 기록 유지
        conversationHistory.put(userId, messages);

        return DiaryConverter.toVoiceChatResultDTO(aiChat);
    }

    @Override
    @Transactional
    public DiaryResponseDTO.SummaryResultDTO createDiaryByVoice(DiaryRequestDTO.SummaryDTO request, Long userId) {

        // 유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 일기 날짜 검사
        checkDiaryDate(userId, request.getDate());

        // 기존 대화 목록 가져오기
//        List<Message> messages = conversationHistory.get(userId);
        List<Message> originalMessages = conversationHistory.get(userId);

        // 대화 기록에서 대화 시작전 LLM모델에게 설명하는 부분 제거
        List<Message> messages = new ArrayList<>();
        if (originalMessages != null) {
            messages = originalMessages.stream()
                    .filter(msg -> !(msg.getRole().equals("system") &&
                            msg.getContent().contains("너는 사용자와 대화하며 일기를 작성하는 챗봇이야.")))
                    .collect(Collectors.toList());
        }


        // 기존 대화 내용 길이 체크 추가
        int totalContentLength = messages.stream()
                .filter(msg -> msg.getRole().equals("user"))
                .mapToInt(msg -> msg.getContent().length())
                .sum();

        String summaryPrompt; // 대화 길이별 프롬프트 선택용
        String selectedModel; // 대화 길이별 모델 선택용

        if (totalContentLength < 20) { // 사용자 입력이 20자 미만인 경우
            selectedModel = summaryModel;
            summaryPrompt = "다음의 내용은 사용자가 AI와 대화한 기록이야." +
                    " role: user인 경우 사용자의 말이고, role: assistant인 경우 AI가 사용자의 말에 대답하는 말이야." +
                    " 이 기록을 바탕으로 사용자가 혼자 쓰는 일기처럼 1인칭 시점으로 작성해줘." +
                    " 대화 내용이 매우 짧으므로 추측하여 내용을 확장하지 말고, 주어진 정보만으로 1-2문장의 간단한 일기로 작성해줘." +
                    " '~했다', '~였다'로 끝나는 문체를 사용하고, AI와 대화했다는 사실이 드러나지 않도록 해줘.";
        } else {
            selectedModel = fineTunedSummaryModel;
            summaryPrompt = "다음의 내용은 사용자가 AI와 대화한 기록이야." +
                    " role: user인 경우 사용자의 말이고, role: assistant인 경우 AI가 사용자의 말에 대답하는 말이야." +
                    " 이 기록을 바탕으로 사용자가 혼자 쓰는 일기처럼 1인칭 시점으로 작성해줘." +
                    " 기본적으로는 '~했다', '~였다'로 끝나는 문체를 사용하고," +
                    " '~걸까?', '~는구나' 같은 혼잣말은 전체 글에서 1-2번 정도만 자연스럽게 사용해줘." +
                    " 사용자의 감정이 자연스럽게 드러나도록 작성하되, 실제 대화 내용에만 기반해줘." +
                    " 일기에서 AI와 대화했다는 사실이 드러나지 않도록 하고, 날짜는 적지 말아줘.";
        }

        messages.add(new Message("system", summaryPrompt));

        String aiChat = requestGptChat(selectedModel, messages);

        //일기 생성
        Diary diary = Diary.builder()
                .content(aiChat)
                .user(user)
                .date(request.getDate())
                .build();

        //일기 저장
        diary = diaryRepository.save(diary);

        // 사용자의 크레딧 수 증가
        CreditGrantResult result = creditUtil.grantDiaryCredit(user, diary);

        // 대화 기록 삭제
        conversationHistory.remove(userId);

        return DiaryConverter.toSummaryResultDTO(diary, result);
    }

    private void checkDiaryDate(Long userId, LocalDate date) {
        // 오늘 이후의 날짜를 선택한 경우
        if (date.isAfter(LocalDate.now())) {
            throw new DiaryHandler(ErrorStatus.DATE_IS_AFTER);
        }
        // 이미 해당 날짜에 작성된 일기가 존재하는 경우
        if (diaryRepository.existsByUserIdAndDate(userId, date)) {
            throw new DiaryHandler(ErrorStatus.DIARY_ALREADY_EXISTS);
        }
    }

    // ‼️ 정상적으로 테스트되는지 확인 필요
    private String requestGptChat(String model, List<Message> messages) {
        // ChatGPT 요청 생성
        ChatGPTRequest gptRequest = new ChatGPTRequest(model, messages);

        // API 요청 및 응답 처리
        ChatGPTResponse res = template.postForObject(apiURL, gptRequest, ChatGPTResponse.class);

        if (res.getChoices() == null || res.getChoices().isEmpty() || res.getChoices().get(0) == null || res.getChoices().get(0).getMessage() == null) {
            throw new OpenAIHandler(ErrorStatus.GPT_RESPONSE_EMPTY);
        }

        return res.getChoices().get(0).getMessage().getContent();
    }

    // 일기 분석
    @Override
    public DiaryResponseDTO.AnalyzedDiaryResponseDTO analyze(Long diaryId) {
        // 1. 일기 조회
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new DiaryHandler(ErrorStatus.DIARY_NOT_FOUND));


        //2. 예외 체크
        //일기 분석은 1번만 가능
        if(diary.getDiaryKeywords() != null && !diary.getDiaryKeywords().isEmpty()) {
            throw new DiaryHandler(ErrorStatus.ANALYZED_DIARY);
        }


        // 3. OpenAI API 호출하여 감정 반환 & 반환받은 감정 체크 (3개인지, 중복안되었는지)
        List<Keyword> analyzedEmotions = openAIAnalyzeDiary(diary.getContent());
        log.info("[최종 분석된 감정] : " + analyzedEmotions.stream().map(Keyword::getName).toList().toString());


        // 5. 일기와 감정키워드 연관관계 설정
        setDiaryKeyword(diary, analyzedEmotions);


        // 6. 감정 키워드 2개 랜덤 선택
        List<Keyword> selectedKeyword = selectRandomKeyword(analyzedEmotions);
        log.info("[선택된 감정] : " + selectedKeyword.stream().map(Keyword::getName).toList().toString());


        // 7. 연관관계 맺고 있는 챌린지 중에서 각 랜덤으로 1개씩 선택 (=데일리 챌린지)
        List<Challenge> dailyChallenges = selectDailyChallenge(selectedKeyword);
        log.info("[데일리 챌린지] : " + dailyChallenges.stream().map(Challenge::getTitle).toList().toString());


        // 8. 7)에서 선택되지 않은 챌린지들 중에서 랜덤으로 1개 선택 (=랜덤 챌린지)
        Challenge randomChallenge = challengeRepository.findRandomRemainingChallenge(dailyChallenges);
        log.info("[랜덤 챌린지] : " + randomChallenge.getTitle());


        // 9. converter작업
        return DiaryConverter.toAnalyzedDiaryDTO(analyzedEmotions, dailyChallenges, randomChallenge);
    }


    // 일기 분석 (일기 -> 감정)
    private List<Keyword> openAIAnalyzeDiary(String diaryContent) {

        // 프롬프트
        String prompt = String.format("""
            [작업]
            [일기]분석을 진행하여, 감정 3개를 응답으로 주세요. 응답은 형용사 형태의 감정들로 구성되어야 합니다.
            
            [수행]
            [일기]: %s
            
            [응답 예시]
            [감정1, 감정2, 감정3]
            """, diaryContent);

        Float temperature = 0.0f;

        // API 요청 생성
        ChatGPTRequest gptRequest = new ChatGPTRequest(keywordModel, prompt, temperature);

        // API 요청 및 응답 처리
        ChatGPTResponse chatGPTResponse = subTemplate.postForObject(apiURL, gptRequest, ChatGPTResponse.class);
        if (chatGPTResponse.getChoices().isEmpty()) {
            throw new OpenAIHandler(ErrorStatus.GPT_RESPONSE_EMPTY);
        }

        String result = chatGPTResponse.getChoices().get(0).getMessage().getContent();
        log.info("[GPT 결과] : " + result);

        // 응답 결과 정리 ([]를 제거하고 각 감정을 분리)
        result = result.replace("[", "").replace("]", "").trim(); // 괄호 제거 후 양 옆 공백 제거
        log.info("[결과정리1] : " + result);

        List<String> emotionsList = Arrays.asList(result.split("\\s*,\\s*")); // , 앞뒤 공백 제거하고 감정들을 나누기
        log.info("[결과정리2] : " + emotionsList.toString());


        // 결과 체크
        // 3개인지 체크
        if (emotionsList.size() != 3) {
            throw new OpenAIHandler(ErrorStatus.EMOTIONS_COUNT_INVALID);
        }

        // 중복 체크
        Set<String> uniqueEmotions = new HashSet<>(emotionsList);
        if (uniqueEmotions.size() != emotionsList.size()) {
            throw new OpenAIHandler(ErrorStatus.EMOTIONS_DUPLICATE);
        }

        // 유사도 분석을 통해 DB의 감정으로 변환 후 반환
        return computeSimilarity(emotionsList);
    }


    // 유사도 분석을 통해 DB의 감정으로 변환
    private List<Keyword> computeSimilarity(List<String> inputEmotions) {

        FlaskRequestDTO.EmotionAnalysisRequestDTO request = FlaskConverter.toEmotionAnalysisRequestDTO(inputEmotions);
        List<Keyword> result = new ArrayList<>();

        // Flask에 모든 감정을 전달하여 유사도 분석 요청
        log.info("[입력된 감정을 Flask로 전달하여 분석]");

        ResponseEntity<FlaskResponseDTO.EmotionAnalysisResponseDTO> response = template.postForEntity(
                "http://localhost:5000/analyze_emotions",
                request,
                FlaskResponseDTO.EmotionAnalysisResponseDTO.class);

        FlaskResponseDTO.EmotionAnalysisResponseDTO body = response.getBody();

        // 응답 오류체크
        if (body.getAnalyzedEmotions() == null || body.getAnalyzedEmotions().isEmpty()) {
            throw new FlaskHandler(ErrorStatus.FLASK_API_CALL_FAILED);
        }

        List<FlaskResponseDTO.SimilarityResultDTO> analyzedEmotions = body.getAnalyzedEmotions();

        for (FlaskResponseDTO.SimilarityResultDTO analysisResult : analyzedEmotions) {

            String inputEmotion = analysisResult.getInputEmotion();
            String similarEmotion = analysisResult.getSimilarEmotion();
            Double similarityScore = analysisResult.getSimilarityScore();

            // Flask에서 받은 정보 출력
            log.info("[Flask 분석 결과] - 입력 감정 : {}, 유사 감정 : {}, 유사도 점수 : {}", inputEmotion, similarEmotion, similarityScore);

            keywordRepository.findByName(similarEmotion)
                    .ifPresentOrElse(result::add,
                            () -> { throw new KeywordHandler(ErrorStatus.KEYWORD_NOT_FOUND); });
        }

        return result;
    }


    // 일기와 감정키워드 연관관계 설정
    private void setDiaryKeyword(Diary diary, List<Keyword> analyzedEmotions) {
        // 각 Keyword에 대해 DiaryKeyword 추가
        for (Keyword keyword : analyzedEmotions) {
            DiaryKeyword diaryKeyword = DiaryKeyword.builder()
                    .diary(diary)
                    .keyword(keyword)
                    .build();

            diary.getDiaryKeywords().add(diaryKeyword); // Diary에 DiaryKeyword 추가
        }

        diaryRepository.save(diary);
    }


    // 감정키워드 랜덤 2개 선택
    private List<Keyword> selectRandomKeyword(List<Keyword> analyzedEmotions) {
        Random random = new Random();

        List<Keyword> editableList = new ArrayList<>(analyzedEmotions);
        Collections.shuffle(editableList, random);

        return editableList.subList(0, 2);
    }


    // 데일리챌린지 랜덤 2개 선택
    private List<Challenge> selectDailyChallenge(List<Keyword> selectedKeyword) {
        Random random = new Random();
        List<Challenge> selectedChallenges = new ArrayList<>();

        // 전달받은 selectedKeyword를 통해 연관된 챌린지들 조회
        for (Keyword keyword : selectedKeyword) {
            // 매핑 테이블을 통해 해당 키워드와 연관된 챌린지들을 가져오기
            List<ChallengeKeyword> challengeKeywords = challengeKeywordRepository.findByKeywordWithChallenge(keyword);

            // 연관된 챌린지들
            List<Challenge> relatedChallenges = challengeKeywords.stream()
                    .map(ChallengeKeyword::getChallenge)
                    .filter(challenge -> !selectedChallenges.contains(challenge))  // 이미 선택된 챌린지 제외
                    .collect(Collectors.toList());

            if (relatedChallenges.isEmpty()) {
                throw new ChallengeHandler(ErrorStatus.RELATED_CHALLENGE_NOT_FOUND);
            }

            Collections.shuffle(relatedChallenges, random);
            Challenge selectedChallenge = relatedChallenges.get(0);
            selectedChallenges.add(selectedChallenge);

            log.info("[" + keyword.getName() + "]과 연관된 [" + selectedChallenge.getTitle() + "]을 데일리 챌린지로 선택");
        }

        return selectedChallenges;
    }
}
