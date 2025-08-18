package umc.GrowIT.Server.service.creditService;

import umc.GrowIT.Server.web.dto.CreditDTO.CreditResponseDTO;

public interface CreditQueryService {
    CreditResponseDTO.CurrentCreditDTO getCurrentCredit(Long userId); //현재 크레딧 조회

    CreditResponseDTO.TotalCreditDTO getTotalCredit(Long userId); //누적 크레딧 조회
}
