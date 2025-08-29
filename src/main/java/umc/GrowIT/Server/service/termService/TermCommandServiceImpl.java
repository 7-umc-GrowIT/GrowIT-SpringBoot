package umc.GrowIT.Server.service.termService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.exception.TermHandler;
import umc.GrowIT.Server.domain.UserTerm;
import umc.GrowIT.Server.repository.UserTermRepository;
import umc.GrowIT.Server.web.dto.TermDTO.TermRequestDTO;

import java.util.List;

import static umc.GrowIT.Server.apiPayload.code.status.ErrorStatus.TERM_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class TermCommandServiceImpl implements TermCommandService {

    private final UserTermRepository userTermRepository;

    @Override
    public void updateUserTerms(List<TermRequestDTO.UserTermDTO> userTermDTO) {
        userTermDTO.forEach(userTerm -> {
            UserTerm currentUserTerm = userTermRepository.findByTermId(userTerm.getTermId())
                    .orElseThrow(() -> new TermHandler(TERM_NOT_FOUND));
            currentUserTerm.updateAgreed(userTerm.getAgreed());
        });
    }
}
