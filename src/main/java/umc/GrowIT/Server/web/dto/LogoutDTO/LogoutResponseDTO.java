package umc.GrowIT.Server.web.dto.LogoutDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoutResponseDTO {
    private String message; // "로그아웃이 완료되었습니다."
}
