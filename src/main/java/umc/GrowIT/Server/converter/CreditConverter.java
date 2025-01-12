package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.web.dto.CreditDTO.CreditResponseDTO;

public class CreditConverter {

    public static CreditResponseDTO.CreditDTO toCurrentCreditDTO(Integer currentCredit) {
        return CreditResponseDTO.CreditDTO.builder()
                .credit(currentCredit)
                .build();
    }

    // User 엔티티로부터 직접 변환하는 메서드도 추가
    public static CreditResponseDTO.CreditDTO toCreditDTOFromUser(User user) {
        return CreditResponseDTO.CreditDTO.builder()
                .credit(user.getCurrentCredit())
                .build();
    }
}