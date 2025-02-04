package umc.GrowIT.Server.service.userService;

import umc.GrowIT.Server.domain.CustomUserDetails;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;


public interface UserCommandService {

    UserResponseDTO.TokenDTO createUser(UserRequestDTO.UserInfoDTO userInfoDTO);

    UserResponseDTO.TokenDTO emailLogin(UserRequestDTO.EmailLoginDTO emailLoginDTO);

    void updatePassword(UserRequestDTO.PasswordDTO passwordDTO);

    UserResponseDTO.DeleteUserResponseDTO delete(Long userId);

    CustomUserDetails createUserDetails(User user);

    void setRefreshToken(String refreshToken, User user);

    UserResponseDTO.TokenDTO performAuthentication(String email, String password);
}
