package umc.GrowIT.Server.web.dto.CreditPaymentDTO;

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
public class CreditPaymentRequestDTO {

    @NotNull
    @Schema(description = "Base64 인코딩된 앱스토어 영수증 데이터")
    private String receiptData;

    @NotNull
    @Schema(description = "Apple에서 발급한 거래 ID", example = "1000000123456789")
    private String transactionId;

    @NotNull
    @Schema(description = "인앱구매 상품 ID", example = "credits_100")
    private String productId;

    @NotNull
    @Schema(description = "구매한 크레딧 수량", example = "100")
    private Integer creditAmount;

    @NotNull
    @Schema(description = "결제 금액 (원)", example = "2500")
    private Integer amount;
}
