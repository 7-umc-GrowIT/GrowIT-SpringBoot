package umc.GrowIT.Server.service.GroService;

import umc.GrowIT.Server.web.dto.GroDTO.GroRequestDTO;
import umc.GrowIT.Server.web.dto.GroDTO.GroResponseDTO;

public interface GroCommandService {
    GroResponseDTO.CreateResponseDTO createGro(Long userId, String nickname, String backgroundItem);

    GroResponseDTO.NicknameResponseDTO updateNickname(Long userId, GroRequestDTO.NicknameRequestDTO nicknameDTO);
}
