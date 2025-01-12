package umc.GrowIT.Server.service;

import umc.GrowIT.Server.web.dto.TermDTO.TermResponseDTO;

import java.util.List;

public interface TermQueryService {

    List<TermResponseDTO.TermDTO> getTerms();
}
