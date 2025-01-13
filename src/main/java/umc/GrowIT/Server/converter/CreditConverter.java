package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.web.dto.CreditDTO.CreditResponseDTO;

public class CreditConverter {
    public static CreditResponseDTO.CurrentCreditDTO toCurrentCreditDTO(Integer currentCredit) {
        return CreditResponseDTO.CurrentCreditDTO.builder()
                .currentCredit(currentCredit)
                .build();
    }

    public static CreditResponseDTO.TotalCreditDTO toTotalCreditDTO(Integer totalCredit) {
        return CreditResponseDTO.TotalCreditDTO.builder()
                .totalCredit(totalCredit)
                .build();
    }
}