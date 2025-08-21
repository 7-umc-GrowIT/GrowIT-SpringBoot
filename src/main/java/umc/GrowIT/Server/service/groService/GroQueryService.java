package umc.GrowIT.Server.service.groService;

import umc.GrowIT.Server.web.dto.GroDTO.GroResponseDTO;

public interface GroQueryService {
    GroResponseDTO.GroAndEquippedItemsDTO getGroAndEquippedItems(Long userId);
}
