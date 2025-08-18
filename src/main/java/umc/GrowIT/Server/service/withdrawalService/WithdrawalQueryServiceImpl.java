package umc.GrowIT.Server.service.withdrawalService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.converter.WithdrawalConverter;
import umc.GrowIT.Server.domain.WithdrawalReason;
import umc.GrowIT.Server.repository.WithdrawalReasonRepository;
import umc.GrowIT.Server.web.dto.WithdrawalDTO.WithdrawalResponseDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WithdrawalQueryServiceImpl implements WithdrawalQueryService {

    private final WithdrawalReasonRepository withdrawalReasonRepository;

    @Override
    @Transactional(readOnly = true)
    public List<WithdrawalResponseDTO.WithdrawalReasonDTO> getWithdrawalReasons() {
        // 1. DB 조회
        List<WithdrawalReason> withdrawalReasons = withdrawalReasonRepository.findAll();

        // 2. converter 작업 후 반환
        return WithdrawalConverter.toWithdrawalReasonDTOList(withdrawalReasons);
    }
}
