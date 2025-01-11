package umc.GrowIT.Server.web.dto.PaymentDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    @Schema(
            description = "결제 후 현재 크레딧",
            example = "150"  // 기존 50 크레딧에 100 크레딧 추가된 경우
    )
    private Integer currentCredit;

    @Schema(
            description = "결제 후 누적 크레딧",
            example = "300"  // 기존 200 크레딧에 100 크레딧 추가된 경우
    )
    private Integer totalCredit;

    @Schema(
            description = "결제 상태",
            example = "SUCCESS",
            allowableValues = {"SUCCESS", "FAILED", "PENDING"}
    )
    private String status;

    @Schema(
            description = "결제 시간",
            example = "2024-01-11T15:30:00"
    )
    private String paidAt;
}
