package umc.GrowIT.Server.service;

import umc.GrowIT.Server.dto.TermResponseDTO;

import java.util.List;

public interface TermQueryService {

    List<TermResponseDTO.TermDTO> getTerms();
}
