package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.domain.Diary;
import umc.GrowIT.Server.domain.Keyword;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.enums.DiaryStatus;
import umc.GrowIT.Server.domain.enums.DiaryType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;
import umc.GrowIT.Server.web.dto.KeywordDTO.KeywordResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DiaryConverter {
    public static DiaryResponseDTO.DiaryDateDTO toDiaryDateDTO(Diary diary){
        return DiaryResponseDTO.DiaryDateDTO.builder()
                .diaryId(diary.getId())
                .date(diary.getDate())
                .build();
    }

    public static DiaryResponseDTO.DiaryDateListDTO toDiaryDateListDTO(List<Diary> diaryList){

        List<DiaryResponseDTO.DiaryDateDTO> diaryDateDTOList = diaryList.stream()
                .map(DiaryConverter::toDiaryDateDTO).collect(Collectors.toList());

        return DiaryResponseDTO.DiaryDateListDTO.builder()
                .diaryDateList(diaryDateDTOList)
                .listSize(diaryDateDTOList.size())
                .build();
    }

    public static DiaryResponseDTO.DiaryDTO toDiaryDTO(Diary diary){
        //일기의 id, 내용, 생성일
        return DiaryResponseDTO.DiaryDTO.builder()
                .diaryId(diary.getId())
                .content(diary.getContent())
                .date(diary.getDate())
                .build();
    }

    public static DiaryResponseDTO.DiaryListDTO toDiaryListDTO(List<Diary> diaryList){

        List<DiaryResponseDTO.DiaryDTO> diaryDTOList = diaryList.stream()
                .map(DiaryConverter::toDiaryDTO).collect(Collectors.toList());

        return DiaryResponseDTO.DiaryListDTO.builder()
                .diaryList(diaryDTOList)
                .listSize(diaryDTOList.size())
                .build();
    }

    public static DiaryResponseDTO.ModifyDiaryResultDTO toModifyResultDTO(Diary diary){

        return DiaryResponseDTO.ModifyDiaryResultDTO.builder()
                .diaryId(diary.getId())
                .content(diary.getContent())
                .build();
    }

    public static DiaryResponseDTO.SaveDiaryResultDTO toSaveResultDTO(Diary diary){
        return DiaryResponseDTO.SaveDiaryResultDTO.builder()
                .diaryId(diary.getId())
                .date(diary.getDate())
                .build()
                ;
    }

    public static DiaryResponseDTO.VoiceChatResultDTO toVoiceChatResultDTO(String aiChat){

        return DiaryResponseDTO.VoiceChatResultDTO.builder()
                .chat(aiChat)
                .build();
    }


    // 챌린지 추천
    public static DiaryResponseDTO.AnalyzedDiaryResponseDTO toAnalyzedDiaryDTO(List<Keyword> analyzedEmotions, List<Challenge> dailyChallenges, Challenge randomChallenge) {
        // 감정키워드 변환
        List<KeywordResponseDTO.KeywordDTO> emotionKeywordsDTOs = KeywordConverter.toKeywordsDTO(analyzedEmotions);

        // 챌린지 변환
        List<ChallengeResponseDTO.ChallengeDTO> recommendedChallenges = ChallengeConverter.toRecommendedChallenges(dailyChallenges, randomChallenge);

        // 최종 response
        return DiaryResponseDTO.AnalyzedDiaryResponseDTO.builder()
                .emotionKeywords(emotionKeywordsDTOs)
                .recommendedChallenges(recommendedChallenges)
                .build();
    }

    public static Diary toDiary(String content, LocalDate date, User user, DiaryType type) {
        return Diary.builder()
                .content(content)
                .user(user)
                .date(date)
                .status(DiaryStatus.PENDING)
                .type(type)
                .build()
                ;
    }
}
