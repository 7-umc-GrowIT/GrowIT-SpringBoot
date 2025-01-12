package umc.GrowIT.Server.service.CreditService;

import umc.GrowIT.Server.web.dto.CreditDTO.CreditResponseDTO;

public interface CreditQueryService {
    CreditResponseDTO.CreditDTO getCurrentCredit(Long userId); //현재 크레딧 조회

    CreditResponseDTO.CreditDTO getTotalCredit(Long userId); //누적 크레딧 조회
}
