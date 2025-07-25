package umc.GrowIT.Server.web.dto.AppleValidationDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class AppleValidationDTO {
    private final boolean isValid;
    private final String originalTransactionId;
    private final String purchaseDate;
    private final String bundleId;
    private final String environment;
    private final String fullResponse;
    private final String errorMessage;


    // 성공 응답 생성
    public static AppleValidationDTO success(String originalTransactionId, String purchaseDate,
                                             String bundleId, String environment, String fullResponse) {
        return new AppleValidationDTO(true, originalTransactionId, purchaseDate,
                bundleId, environment, fullResponse, null);
    }

    // 실패 응답 생성
    public static AppleValidationDTO failure(String errorMessage, String fullResponse) {
        return new AppleValidationDTO(false, null, null, null, null, fullResponse, errorMessage);
    }
}
