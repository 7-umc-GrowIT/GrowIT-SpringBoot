package umc.GrowIT.Server.web.dto.PaymentDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    @NotNull
    @Schema(
            description = "결제 고유 ID",
            example = "imp_123456789"
    )
    private String paymentId;

    @NotNull
    @Schema(
            description = "결제 금액",
            example = "10000"
    )
    private Integer amount;

    @NotNull
    @Schema(
            description = "구매한 크레딧 양",
            example = "100"
    )
    private Integer creditAmount;


    private String paymentMethod;
}
