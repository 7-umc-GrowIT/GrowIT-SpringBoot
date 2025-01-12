package umc.GrowIT.Server.service;

import umc.GrowIT.Server.web.dto.TermDTO.TermRequestDTO;

import java.util.List;


public interface TermCommandService {

    void createUserTerms(List<TermRequestDTO.UserTermDTO> userTermDTO);
}
