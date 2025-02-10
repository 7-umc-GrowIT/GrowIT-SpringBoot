package umc.GrowIT.Server.service.diaryService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.DiaryHandler;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;
import umc.GrowIT.Server.converter.DiaryConverter;
import umc.GrowIT.Server.domain.Diary;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.repository.diaryRepository.DiaryRepository;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryRequestDTO;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;
import umc.GrowIT.Server.web.dto.OpenAIDTO.ChatGPTRequest;
import umc.GrowIT.Server.web.dto.OpenAIDTO.ChatGPTResponse;
import umc.GrowIT.Server.web.dto.OpenAIDTO.Message;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DiaryCommandServiceImpl implements DiaryCommandService{
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    @Value("${openai.model1}")
    private String chatModel;

    @Value("${openai.model2}")
    private String summaryModel;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

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

        return DiaryConverter.toCreateResultDTO(diary);
    }

    @Override
    public DiaryResponseDTO.DeleteDiaryResultDTO deleteDiary(Long diaryId, Long userId) {
        //유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Optional<Diary> optionalDiary = diaryRepository.findByUserIdAndId(userId, diaryId);
        Diary diary = optionalDiary.orElseThrow(()->new DiaryHandler(ErrorStatus.DIARY_NOT_FOUND));

        diaryRepository.delete(diary);

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
            messages.add(new Message("system", "너는 사용자와 대화하며 일기를 작성하는 챗봇이야. role: user인 경우 사용자의 말이고, role: assistant인 경우 너가 사용자의 말에 대답하는 말이야. 사용자와의 대화 내용을 기억하고 사용자의 마지막말에 대해 상황에 맞게 대답해줘. 그리고 사용자의 말에 공감하며 가끔씩 감정을 이끌어내는 질문을 해야해. 반드시 존댓말을 사용하고 맞춤법은 제대로 맞춰줘."));
        }

        // 사용자의 입력을 대화 목록에 추가
        messages.add(new Message("user", userChat));

        // ChatGPT 요청 생성
        ChatGPTRequest gptRequest = new ChatGPTRequest(chatModel, messages);

        // API 요청 및 응답 처리
        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, gptRequest, ChatGPTResponse.class);

        if (chatGPTResponse == null || chatGPTResponse.getChoices().isEmpty()) {
            return DiaryConverter.toVoiceChatResultDTO("응답이 없습니다.");
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
        messages.add(new Message("system", "다음의 내용은 사용자가 AI와 대화한 기록이야. role: user인 경우 사용자의 말이고, role: assistant인 경우 AI가 사용자의 말에 대답하는 말이야. 이 기록을 바탕으로 사용자가 직접 작성한 것 처럼 1인칭 시점으로 일기를 작성해줘. 사용자의 감정이 풍부하게 드러나도록 작성해줘. 또한 일기에서 AI와 대화했다는 사실이 드러나지 않도록 작성해줘."));

        // ChatGPT 요청 생성
        ChatGPTRequest gptRequest = new ChatGPTRequest(summaryModel, messages);

        // API 요청 및 응답 처리
        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, gptRequest, ChatGPTResponse.class);

        if (chatGPTResponse == null || chatGPTResponse.getChoices().isEmpty()) {
            //Todo: 응답이 없을 때 예외 처리
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

        // 대화 기록 삭제
        conversationHistory.remove(userId);

        return DiaryConverter.toSummaryResultDTO(diary);
    }


}
