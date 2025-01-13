package umc.GrowIT.Server.web.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class UserResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenDTO {
        private String accessToken;
        private String refreshToken;
    }

    // 이메일 인증 전송 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendAuthEmailResponseDTO {
        // TODO 디테일하게 결정 필요
        private String message; // ex) 인증 이메일이 발송되었습니다
    }

    // 인증 코드 확인 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerifyAuthCodeResponseDTO {
        // TODO 디테일하게 결정 필요
        private String message; // ex) 인증이 완료되었습니다, 인증번호가 올바르지 않습니다
    }

    // 비밀번호 재설정 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResetPasswordResponseDTO {
        // TODO 디테일하게 결정 필요
        private String message; // ex) 비밀번호 변경이 완료되었습니다
    }

    // 회원 탈퇴 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteUserResponseDTO {
        // TODO 디테일하게 결정 필요
        private String message; // ex) 회원 탈퇴가 완료되었습니다
    }

    // 보유 중인 아이템 조회 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetOwnedItemsResponseDTO {
        private List<ItemDTO> items;  // 아이템 목록

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ItemDTO {
            private Long itemId;    // 아이템 ID
            private String itemName; // 아이템 이름
            private String itemStatus; // 아이템 상태 (착용 여부)
        }
    }

}
