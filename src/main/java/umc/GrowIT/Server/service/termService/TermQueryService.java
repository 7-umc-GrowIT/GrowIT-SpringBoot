package umc.GrowIT.Server.service.termService;

import umc.GrowIT.Server.domain.Term;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserTerm;
import umc.GrowIT.Server.web.dto.TermDTO.TermRequestDTO;
import umc.GrowIT.Server.web.dto.TermDTO.TermResponseDTO;

import java.util.List;

public interface TermQueryService {

    List<TermResponseDTO.TermDTO> getTerms();
    List<UserTerm> checkUserTerms(List<TermRequestDTO.UserTermDTO> requestedUserTerms, User newUser);
}
