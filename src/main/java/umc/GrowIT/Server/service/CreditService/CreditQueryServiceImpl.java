package umc.GrowIT.Server.service.CreditService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.converter.CreditConverter;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.repository.CreditRepository.CreditRepository;
import umc.GrowIT.Server.web.dto.CreditDTO.CreditResponseDTO;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreditQueryServiceImpl implements CreditQueryService {
    private final CreditRepository creditRepository;

    public CreditResponseDTO.CreditDTO getCurrentCredit(Long userId){

        Integer currentCredit = creditRepository.findCreditByUserId(userId);

        return CreditConverter.toCurrentCreditDTO(currentCredit);
    }
}
