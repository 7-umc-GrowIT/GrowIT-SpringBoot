package umc.GrowIT.Server.service.withdrawalService;

import umc.GrowIT.Server.web.dto.WithdrawalDTO.WithdrawalResponseDTO;

import java.util.List;

public interface WithdrawalQueryService {
    List<WithdrawalResponseDTO.WithdrawalReasonDTO> getWithdrawalReasons();
}
