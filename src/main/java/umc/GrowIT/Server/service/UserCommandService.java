package umc.GrowIT.Server.service;

import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;


public interface UserCommandService {

    UserResponseDTO.TokenDTO createUser(UserRequestDTO.UserInfoDTO userInfoDTO);

    UserResponseDTO.TokenDTO emailLogin(UserRequestDTO.EmailLoginDTO emailLoginDTO);

    void updatePassword(UserRequestDTO.PasswordDTO passwordDTO);
}
