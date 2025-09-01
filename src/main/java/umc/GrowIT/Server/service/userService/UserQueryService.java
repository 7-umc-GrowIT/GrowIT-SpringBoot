package umc.GrowIT.Server.service.userService;

import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

public interface UserQueryService {
    boolean isUserInactive(Long userId);

    UserResponseDTO.MyPageDTO getMyPage(Long userId);
}
