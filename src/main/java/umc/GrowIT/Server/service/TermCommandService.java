package umc.GrowIT.Server.service;

import umc.GrowIT.Server.dto.TermRequestDTO;

import java.util.List;


public interface TermCommandService {

    void createUserTerms(List<TermRequestDTO.UserTermDTO> userTermDTO);
}
