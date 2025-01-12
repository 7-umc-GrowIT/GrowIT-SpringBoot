package umc.GrowIT.Server.service.termService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.GrowIT.Server.converter.TermConverter;
import umc.GrowIT.Server.domain.Term;
import umc.GrowIT.Server.web.dto.TermDTO.TermResponseDTO;
import umc.GrowIT.Server.repository.TermRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermQueryServiceImpl implements TermQueryService{

    private final TermRepository termRepository;

    public List<TermResponseDTO.TermDTO> getTerms(){
        List<Term> terms = termRepository.findAll();

        return terms.stream()
                .map(TermConverter::toTermDTO)
                .collect(Collectors.toList());
    }
}
