package umc.GrowIT.Server.service.CreditService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.converter.CreditConverter;
import umc.GrowIT.Server.repository.CreditRepository.UserCreditRepository;
import umc.GrowIT.Server.web.dto.CreditDTO.CreditResponseDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreditQueryServiceImpl implements CreditQueryService {
    private final UserCreditRepository creditRepository;

    public CreditResponseDTO.CurrentCreditDTO getCurrentCredit(Long userId){

        Integer currentCredit = creditRepository.findCurrentCreditById(userId);;

        return CreditConverter.toCurrentCreditDTO(currentCredit);
    }

    public CreditResponseDTO.TotalCreditDTO getTotalCredit(Long userId){
        Integer totalCredit = creditRepository.findTotalCreditById(userId);

        return CreditConverter.toTotalCreditDTO(totalCredit);
    }
}
