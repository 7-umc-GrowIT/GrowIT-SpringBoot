package umc.GrowIT.Server.service.GroService;

import umc.GrowIT.Server.web.dto.GroDTO.GroResponseDTO;

public interface GroCommandService {
    public GroResponseDTO.CreateResponseDTO createGro(Long userId, String nickname, String backgroundItem);

    GroResponseDTO.GroAndEquippedItemsDTO getGroAndEquippedItems(Long userId);
}
