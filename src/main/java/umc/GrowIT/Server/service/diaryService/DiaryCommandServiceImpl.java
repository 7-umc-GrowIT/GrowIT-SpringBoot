package umc.GrowIT.Server.service.diaryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.*;
import umc.GrowIT.Server.converter.DiaryConverter;
import umc.GrowIT.Server.domain.*;
import umc.GrowIT.Server.repository.*;
import umc.GrowIT.Server.repository.diaryRepository.DiaryRepository;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryRequestDTO;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;
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
    private final UserChallengeRepository userChallengeRepository;
    //일기 작성 시 추가되는 크레딧 개수
    private Integer diaryCredit = 2;

    @Value("${openai.model1}")
    private String chatModel;

    @Value("${openai.model2}")
    private String summaryModel;

    @Value("${openai.keyword-model}")
    private String keywordModel;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @Autowired
    private RestTemplate keywordModelTemplate;

    //userId-대화내용 저장용 HashMap
    private final Map<Long, List<Message>> conversationHistory = new HashMap<>();
    @Override
    public DiaryResponseDTO.ModifyDiaryResultDTO modifyDiary(DiaryRequestDTO.ModifyDiaryDTO request, Long diaryId, Long userId) {

        //유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Optional<Diary> optionalDiary = diaryRepository.findByUserIdAndId(userId, diaryId);
        Diary diary = optionalDiary.orElseThrow(()->new DiaryHandler(ErrorStatus.DIARY_NOT_FOUND));

        //기존의 내용과 변경되지 않았을 경우
        if(diary.getContent().equals(request.getContent())){
            throw new DiaryHandler(ErrorStatus.DIARY_SAME_CONTENT);
        }

        diary.setContent(request.getContent());

        diaryRepository.save(diary);
        return DiaryConverter.toModifyResultDTO(diary);
    }

    @Override
    public DiaryResponseDTO.CreateDiaryResultDTO createDiary(DiaryRequestDTO.CreateDiaryDTO request, Long userId) {

        //유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        //일기 내용이 100자 이상인지 검사
        if(request.getContent().length()<100){
            throw new DiaryHandler(ErrorStatus.DIARY_CHARACTER_LIMIT);
        }

        //날짜 검사(오늘 이후의 날짜 x)
        if (request.getDate().isAfter(LocalDate.now())) {
            throw new DiaryHandler(ErrorStatus.DATE_IS_AFTER);
        }

        //날짜 검사(이미 해당 날짜에 작성된 일기가 존재)
        if(diaryRepository.existsByUserIdAndDate(userId, request.getDate())){
            throw new DiaryHandler(ErrorStatus.DIARY_ALREADY_EXISTS);
        }

        //일기 생성
        Diary diary = Diary.builder()
                .content(request.getContent())
                .user(user)
                .date(request.getDate())
                .build();

        //일기 저장
        diary = diaryRepository.save(diary);

        //사용자의 크레딧수 증가
        user.updateCurrentCredit(user.getCurrentCredit() + diaryCredit);
        user.updateTotalCredit(user.getTotalCredit() + diaryCredit);
        userRepository.save(user);

        return DiaryConverter.toCreateResultDTO(diary);
    }

    @Override
    public DiaryResponseDTO.DeleteDiaryResultDTO deleteDiary(Long diaryId, Long userId) {
        //유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Optional<Diary> optionalDiary = diaryRepository.findByUserIdAndId(userId, diaryId);
        Diary diary = optionalDiary.orElseThrow(()->new DiaryHandler(ErrorStatus.DIARY_NOT_FOUND));
        LocalDate targetDate = diary.getDate();

        //일기 삭제
        diaryRepository.delete(diary);

        //UserChallenge 조회
        List<UserChallenge> targetUserChallenge = userChallengeRepository.findUserChallengesByDateAndUserId(user.getId(), targetDate);

        //UserChallenge 삭제
        userChallengeRepository.deleteAll(targetUserChallenge);


        return DiaryConverter.toDeleteResultDTO(diary);
    }

    @Override
    public DiaryResponseDTO.VoiceChatResultDTO chatByVoice(DiaryRequestDTO.VoiceChatDTO request, Long userId) {

        //유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        String userChat = request.getChat();

        conversationHistory.putIfAbsent(userId, new ArrayList<>());

        // 기존 대화 목록 가져오기
        List<Message> messages = conversationHistory.get(userId);

        //처음 대화라면 시스템 프롬프트 추가
        if(messages.isEmpty()){
            messages.add(new Message("system",
                    "당신은 사용자의 하루 이야기를 공감으로 받아주는 AI입니다.\n\n" +
                            "다음 규칙을 반드시 따르세요:\n" +
                            "1. 절대 질문하지 마세요. 물음표(?)를 포함한 문장도 금지입니다.\n" +
                            "2. '~했나요?', '~무엇인가요?', '~어떠셨나요?' 같은 의문형 표현도 사용하지 마세요.\n" +
                            "3. 감정이나 생각을 유도하는 문장도 금지입니다. 예: '어떤 기분이셨나요?', '무슨 생각이 드셨나요?'\n" +
                            "4. 공감과 긍정적인 피드백만 해주세요. 사용자에게 아무것도 묻지 마세요.\n" +
                            "5. 대화를 이어가려 하지 말고, 사용자의 메시지에 대해 짧고 따뜻한 반응만 하세요.\n" +
                            "6. 당신은 경청하는 역할이며, 대화 흐름을 주도하지 않습니다.\n" +
                            "사용자 메시지에는 항상 'cnt' 값이 포함되어 있습니다. 반드시 cnt == 3 일 때만, 규칙이 적용되지 않습니다."
            ));

        }

        long userTurn = messages.stream()
                .filter(m -> m.getRole().equals("user"))
                .count() % 3;  // 지금까지 몇 번 user가 말했는지 셈

        String numberedUserChat = "cnt == " + (userTurn + 1) + "\n\n" +
                                  "사용자 메세지 : " + userChat;

        log.info(numberedUserChat);

        // 사용자의 입력을 대화 목록에 추가
        messages.add(new Message("user", numberedUserChat));

        // ChatGPT 요청 생성
        ChatGPTRequest gptRequest = new ChatGPTRequest(chatModel, messages);

        // API 요청 및 응답 처리
        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, gptRequest, ChatGPTResponse.class);

        if (chatGPTResponse == null || chatGPTResponse.getChoices().isEmpty()) {
            throw new OpenAIHandler(ErrorStatus.GPT_RESPONSE_EMPTY);
        }

        String aiChat = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        // ai의 답변을 대화 목록에 추가
        messages.add(new Message("assistant", aiChat));

        // 대화 기록 유지
        conversationHistory.put(userId, messages);

        return DiaryConverter.toVoiceChatResultDTO(aiChat);
    }

    @Override
    public DiaryResponseDTO.SummaryResultDTO createDiaryByVoice(DiaryRequestDTO.SummaryDTO request, Long userId) {

        //유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        //날짜 검사(오늘 이후의 날짜 x)
        if (request.getDate().isAfter(LocalDate.now())) {
            throw new DiaryHandler(ErrorStatus.DATE_IS_AFTER);
        }

        //날짜 검사(이미 해당 날짜에 작성된 일기가 존재)
        if(diaryRepository.existsByUserIdAndDate(userId, request.getDate())){
            throw new DiaryHandler(ErrorStatus.DIARY_ALREADY_EXISTS);
        }

        // 기존 대화 목록 가져오기
        List<Message> messages = conversationHistory.get(userId);

        // AI에게 대화내용 요약 요청
        messages.add(new Message("system", "다음의 내용은 사용자가 AI와 대화한 기록이야." +
                " role: user인 경우 사용자의 말이고, role: assistant인 경우 AI가 사용자의 말에 대답하는 말이야." +
                " 이 기록을 바탕으로 사용자가 직접 작성한 것 처럼 1인칭 시점으로 일기를 작성해줘." +
                " 사용자의 감정이 풍부하게 드러나도록 작성해줘." +
                " 또한 일기에서 AI와 대화했다는 사실이 드러나지 않도록 작성해줘." +
                " 마지막으로 일기에 날짜는 적지 말아줘."));

        // ChatGPT 요청 생성
        ChatGPTRequest gptRequest = new ChatGPTRequest(summaryModel, messages);

        // API 요청 및 응답 처리
        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, gptRequest, ChatGPTResponse.class);

        if (chatGPTResponse == null || chatGPTResponse.getChoices().isEmpty()) {
            throw new OpenAIHandler(ErrorStatus.GPT_RESPONSE_EMPTY);
        }

        String aiChat = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        //일기 생성
        Diary diary = Diary.builder()
                .content(aiChat)
                .user(user)
                .date(request.getDate())
                .build();

        //일기 저장
        diary = diaryRepository.save(diary);

        //사용자의 크레딧수 증가
        user.updateCurrentCredit(user.getCurrentCredit() + diaryCredit);
        user.updateTotalCredit(user.getTotalCredit() + diaryCredit);
        userRepository.save(user);

        // 대화 기록 삭제
        conversationHistory.remove(userId);

        return DiaryConverter.toSummaryResultDTO(diary);
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


        // 3. DB에서 감정들 조회
        List<String> emotions = keywordRepository.findAll()
                .stream()
                .map(emotion -> emotion.getName())
                .toList();
        log.info("[DB 감정들] : " + emotions.toString());


        // 4. OpenAI API 호출하여 감정 반환 & 반환받은 감정 체크 (3개인지, 중복안되었는지, DB와동일한지)
        List<Keyword> analyzedEmotions = openAIAnalyzeDiary(diary.getContent(), emotions.toString());
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
    private List<Keyword> openAIAnalyzeDiary(String diaryContent, String emotions) {

        log.info("[프롬프트의 감정들] : " + emotions);

        // 프롬프트
        String prompt = String.format("""
            [작업]
            [감정 목록]에 있는 범위에 대해 [일기]분석을 진행하여, 감정 목록에 포함된 감정 3개를 응답으로 주세요. 응답은 다음에 제공되는 감정 목록에 있는 것들로 구성되어야 합니다.
            
            [수행]
            [감정 목록]: %s
            [일기]: %s
            
            [응답 예시]
            [감정 목록에 포함된 감정1, 감정 목록에 포함된 감정2, 감정 목록에 포함된 감정3]
            """, emotions, diaryContent);

        Float temperature = 0.0f;

        // API 요청 생성
        ChatGPTRequest gptRequest = new ChatGPTRequest(keywordModel, prompt, temperature);

        // API 요청 및 응답 처리
        ChatGPTResponse chatGPTResponse = keywordModelTemplate.postForObject(apiURL, gptRequest, ChatGPTResponse.class);
        if (chatGPTResponse == null || chatGPTResponse.getChoices().isEmpty()) {
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

        // DB에 존재하는 감정인지 체크 & 없으면 유사도 검색
        List<Keyword> emotionKeywords = checkEmotions(uniqueEmotions.stream().toList());

//         테스트용
//        Set<String> testEmotions = new HashSet<>();
//        testEmotions.add("분노");
//        testEmotions.add("설레는");
//        testEmotions.add("울적한");
//        List<Keyword> emotionKeywords = checkEmotions(testEmotions.stream().toList());

        return emotionKeywords;
    }


    // DB 감정인지 체크 (없으면 유사도 분석)
    private List<Keyword> checkEmotions(List<String> inputEmotions) {
        List<Keyword> result = new ArrayList<>();
        Set<String> dbEmotions = new HashSet<>();
        List<String> needAnalysis = new ArrayList<>();

        // 1) DB에 있는지 체크
        for (String emotion : inputEmotions) {
            keywordRepository.findByName(emotion).ifPresentOrElse(
                    keyword -> {
                        result.add(keyword);
                        dbEmotions.add(emotion);
                    },
                    () -> needAnalysis.add(emotion)
            );
        }

        // 2) DB에 없는 감정이 있다면 Flask에 유사도 분석 요청
        if (!needAnalysis.isEmpty()) {
            log.info("[DB 없는 감정 존재 -> 플라스크 API 요청]");

            Map<String, Object> request = Map.of(
                    "emotions", needAnalysis,
                    "existingEmotions", dbEmotions
            );

            ResponseEntity<Map> response = template.postForEntity(
                    "http://localhost:5000/analyze_emotions",
                    request,
                    Map.class
            );

            List<Map<String, Object>> analyzedEmotions = (List<Map<String, Object>>) response.getBody().get("analyzedEmotions");

            if (analyzedEmotions != null) {
                for (Map<String, Object> analysisResult : analyzedEmotions) {
                    String inputEmotion = (String) analysisResult.get("inputEmotion");
                    String similarEmotion = (String) analysisResult.get("similarEmotion");
                    Double similarityScore = (Double) analysisResult.get("similarityScore");

                    // Flask에서 받은 정보 출력
                    log.info("[Flask 분석 결과] - 입력 감정 : {}, 유사 감정 : {}, 유사도 점수 : {}", inputEmotion, similarEmotion, similarityScore);

                    keywordRepository.findByName(similarEmotion)
                            .ifPresentOrElse(result::add,
                                    () -> { throw new KeywordHandler(ErrorStatus.KEYWORD_NOT_FOUND); });
                }
            }
            else {
                throw new FlaskHandler(ErrorStatus.FLASK_API_CALL_FAILED);
            }
        }

        return result;
    }


    // 일기와 감정키워드 연관관계 설정
    private void setDiaryKeyword(Diary diary, List<Keyword> analyzedEmotions) {
        // Diary의 diaryKeywords 리스트가 null이라면 초기화
        if (diary.getDiaryKeywords() == null) {
            diary.setDiaryKeywords(new ArrayList<>());
        }

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
