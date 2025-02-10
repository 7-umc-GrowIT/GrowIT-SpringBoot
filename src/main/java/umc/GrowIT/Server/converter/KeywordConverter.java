package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.domain.Keyword;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;
import umc.GrowIT.Server.web.dto.KeywordDTO.KeywordResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class KeywordConverter {

    // 챌린지 추천
    public static List<KeywordResponseDTO.KeywordDTO> toKeywordsDTO(List<Keyword> analyzedEmotions) {
        return analyzedEmotions.stream()
                .map(keyword -> KeywordResponseDTO.KeywordDTO.builder()
                        .id(keyword.getId())
                        .keyword(keyword.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
