package umc.GrowIT.Server.service.diaryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiaryCommandServiceImpl implements DiaryCommandService{
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    @Override
    public DiaryResponseDTO.ModifyDiaryResultDTO modifyDiary(DiaryRequestDTO.ModifyDiaryDTO request, Long diaryId, Long userId) {

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

        Optional<Diary> optionalDiary = diaryRepository.findByUserIdAndId(userId, diaryId);
        Diary diary = optionalDiary.orElseThrow(()->new DiaryHandler(ErrorStatus.DIARY_NOT_FOUND));

        diaryRepository.delete(diary);

        return DiaryConverter.toDeleteResultDTO(diary);
    }
}
