package umc.GrowIT.Server.service;

import umc.GrowIT.Server.dto.UserRequestDTO;
import umc.GrowIT.Server.dto.UserResponseDTO;


public interface UserCommandService {

    UserResponseDTO.TokenDTO createUser(UserRequestDTO.UserInfoDTO userInfoDTO);

    UserResponseDTO.TokenDTO emailLogin(UserRequestDTO.EmailLoginDTO emailLoginDTO);

    void updatePassword(UserRequestDTO.PasswordDTO passwordDTO);
}
