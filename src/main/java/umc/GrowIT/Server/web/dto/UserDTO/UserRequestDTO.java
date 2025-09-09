package umc.GrowIT.Server.web.dto.UserDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;
import umc.GrowIT.Server.web.dto.TermDTO.TermRequestDTO;

import java.util.List;


public class UserRequestDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "이메일 회원가입 Request")
    public static class UserInfoDTO {
        @Schema(description = "이메일 인증 상태", example = "true")
        private Boolean isVerified;

        @Schema(description = "가입할 계정의 이메일", example = "GrowIT2025@gmail.com")
        @NotBlank(message = "필수 입력 항목입니다.")
        @Email(message = "이메일 형식에 맞춰주세요.")
        private String email;

        @Schema(description = "가입할 계정의 이름", example = "홍길동")
        @Size(min = 1, max = 20, message = "크기는 1에서 20 사이입니다.")
        private String name;

        @Schema(description = "가입할 계정의 비밀번호", example = "rlarmfh25!@")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,30}$", message = "비밀번호는 8~30자의 영문 대소문자, 숫자, 특수문자로 이루어져야 합니다.")
        private String password;

        @Schema(description = "동의할 약관 항목")
        @NotEmpty(message = "필수 입력 항목입니다.")
        private List<TermRequestDTO.UserTermDTO> userTerms;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "이메일 로그인 Request")
    public static class EmailLoginDTO {

        @Schema(description = "로그인할 계정의 이메일", example = "GrowIT2025@gmail.com")
        @NotBlank(message = "필수 입력 항목입니다.")
        @Email(message = "이메일 형식에 맞춰주세요.")
        private String email;

        @Schema(description = "로그인할 계정의 비밀번호", example = "rlarmfh25!@")
        @Size(min = 8, max = 30, message = "크기는 8에서 30 사이입니다.")
        private String password;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "비밀번호 변경 Request")
    public static class PasswordDTO {
        @Schema(description = "이메일 인증 상태", example = "true")
        private Boolean isVerified;

        @Schema(description = "로그인한 사용자의 이메일", example = "GrowIT2025@gmail.com")
        @NotBlank(message = "필수 입력 항목입니다.")
        @Email(message = "이메일 형식에 맞춰주세요.")
        private String email;

        @Schema(description = "변경하려는 비밀번호 입력", example = "rlarmfh25!@")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,30}$", message = "비밀번호는 8~30자의 영문 대소문자, 숫자, 특수문자로 이루어져야 합니다.")
        private String password;

        @Schema(description = "변경하려는 비밀번호 재입력", example = "rlarmfh25!@")
        @Size(min = 8, max = 30, message = "크기는 8에서 30 사이입니다.")
        private String passwordCheck;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "회원탈퇴 Request")
    public static class DeleteUserRequestDTO {

        @NotNull(message = "탈퇴이유 ID를 입력해야 합니다.")
        @Schema(description = "탈퇴이유 ID", example = "1")
        private Long reasonId;
    }
}
