package umc.GrowIT.Server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class UserRequestDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoDTO {

        @NotBlank(message = "필수 입력 항목입니다.")
        @Email(message = "이메일 형식에 맞춰주세요.")
        private String email;

        @Size(min = 1, max = 20, message = "크기는 1에서 20 사이입니다.")
        private String name;

        @Size(min = 8, max = 30, message =  "크기는 8에서 30 사이입니다.")
        private String password;

        @NotEmpty(message = "필수 입력 항목입니다.")
        private List<TermRequestDTO.UserTermDTO> userTerms;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailLoginDTO {

        @NotBlank(message = "필수 입력 항목입니다.")
        @Email(message = "이메일 형식에 맞춰주세요.")
        private String email;

        @Size(min = 8, max = 30, message = "크기는 8에서 30 사이입니다.")
        private String password;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordDTO {

        @Size(min = 8, max = 30, message = "크기는 8에서 30 사이입니다.")
        private String password;

        @Size(min = 8, max = 30, message = "크기는 8에서 30 사이입니다.")
        private String passwordCheck;
    }

}
