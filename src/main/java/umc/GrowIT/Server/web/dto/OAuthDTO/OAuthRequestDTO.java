package umc.GrowIT.Server.web.dto.OAuthDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.jcip.annotations.NotThreadSafe;
import umc.GrowIT.Server.web.dto.TermDTO.TermRequestDTO;

import java.util.List;

public class OAuthRequestDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OAuthUserInfoAndUserTermsDTO {
        @NotEmpty(message = "필수 입력 항목입니다.")
        private List<TermRequestDTO.UserTermDTO> userTerms;

        private OAuthApiResponseDTO.OAuthUserInfoDTO oauthUserInfo;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SocialLoginDTO {
        @NotNull
        private String code;
        private String nickname;
    }
}
