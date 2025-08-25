package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.WithdrawalLog;
import umc.GrowIT.Server.domain.WithdrawalReason;
import umc.GrowIT.Server.web.dto.WithdrawalDTO.WithdrawalResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class WithdrawalConverter {

    // 탈퇴이유 목록 조회
    public static List<WithdrawalResponseDTO.WithdrawalReasonDTO> toWithdrawalReasonDTOList(List<WithdrawalReason> withdrawalReasons) {
        return withdrawalReasons.stream()
                .map(WithdrawalConverter::toWithdrawalReasonDTO)
                .collect(Collectors.toList())
                ;
    }

    public static WithdrawalResponseDTO.WithdrawalReasonDTO toWithdrawalReasonDTO(WithdrawalReason withdrawalReason) {
        return WithdrawalResponseDTO.WithdrawalReasonDTO.builder()
                .id(withdrawalReason.getId())
                .reason(withdrawalReason.getReason())
                .build()
                ;
    }

    // Entity 변환
    public static WithdrawalLog toWithdrawalLog (User user, WithdrawalReason withdrawalReason) {
        return WithdrawalLog.builder()
                .joinedAt(user.getCreatedAt())
                .withdrawalAt(LocalDateTime.now())
                .withdrawalReason(withdrawalReason)
                .build()
                ;
    }
}
