package umc.GrowIT.Server.service.CreditService;

import umc.GrowIT.Server.web.dto.CreditDTO.CreditResponseDTO;

public interface CreditQueryService {
    CreditResponseDTO.CreditDTO getCurrentCredit(Long userId);
}
