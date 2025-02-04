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

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    private final Map<Long, List<ChatGPTRequest>> conversationHistory = new HashMap<>();
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

        // 사용자 대화 기록이 없으면 초기화
        conversationHistory.putIfAbsent(userId, new ArrayList<>());

        // 기존 대화 목록 가져오기
        List<ChatGPTRequest> messages = conversationHistory.get(userId);

        // 사용자의 입력을 대화 목록에 추가
        messages.add(new ChatGPTRequest("user", userChat));

        // ChatGPT 요청 생성
        ChatGPTRequest gptRequest = new ChatGPTRequest(model, messages.toString());

        // API 요청 및 응답 처리
        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, gptRequest, ChatGPTResponse.class);

        if (chatGPTResponse == null || chatGPTResponse.getChoices().isEmpty()) {
            return DiaryConverter.toVoiceChatResultDTO("응답이 없습니다.");
        }

        String aiChat = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        // AI의 응답을 대화 목록에 추가
        messages.add(new ChatGPTRequest("assistant", aiChat));

        // 대화 기록 유지
        conversationHistory.put(userId, messages);

        System.out.println(messages);

        return DiaryConverter.toVoiceChatResultDTO(aiChat);
    }

    @Override
    public DiaryResponseDTO.SummaryResultDTO createDiaryByVoice(DiaryRequestDTO.SummaryDTO request, Long userId) {

        //유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 기존 대화 목록 가져오기
        List<ChatGPTRequest> messages = conversationHistory.get(userId);

        // AI에게 대화내용 요약
        messages.add(new ChatGPTRequest("user", "지금까지의 대화 내용을 요약해서 일기를 생성해줘. 가능하면 100자 이상으로 만들어줘."));

        // ChatGPT 요청 생성
        ChatGPTRequest gptRequest = new ChatGPTRequest(model, messages.toString());

        // API 요청 및 응답 처리
        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, gptRequest, ChatGPTResponse.class);

        if (chatGPTResponse == null || chatGPTResponse.getChoices().isEmpty()) {
            //Todo: 응답이 없을 때 예외 처리
        }

        String aiChat = chatGPTResponse.getChoices().get(0).getMessage().getContent();

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
                .content(aiChat)
                .user(user)
                .date(request.getDate())
                .build();

        //일기 저장
        diary = diaryRepository.save(diary);

        // 대화 기록 삭제
        conversationHistory.remove(userId);

        System.out.println(messages);

        return DiaryConverter.toSummaryResultDTO(diary);
    }


}
