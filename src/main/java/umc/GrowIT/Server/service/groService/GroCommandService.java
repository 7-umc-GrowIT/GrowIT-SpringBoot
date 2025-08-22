package umc.GrowIT.Server.service.groService;

import umc.GrowIT.Server.web.dto.GroDTO.GroResponseDTO;

public interface GroCommandService {
    GroResponseDTO.CreateResponseDTO createGro(Long userId, String nickname, String backgroundItem);

    void updateNickname(Long userId, String nickname);
}
