package umc.GrowIT.Server.service.termService;

import umc.GrowIT.Server.web.dto.TermDTO.TermRequestDTO;

import java.util.List;


public interface TermCommandService {

    void updateUserTerms(List<TermRequestDTO.UserTermDTO> userTermDTO);
}
