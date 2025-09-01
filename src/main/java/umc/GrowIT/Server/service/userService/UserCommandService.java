package umc.GrowIT.Server.service.userService;

import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;


public interface UserCommandService {

    TokenResponseDTO.TokenDTO signupEmail(UserRequestDTO.UserInfoDTO userInfoDTO);

    TokenResponseDTO.TokenDTO loginEmail(UserRequestDTO.EmailLoginDTO emailLoginDTO);

    void updatePassword(UserRequestDTO.PasswordDTO passwordDTO);

   void withdraw(Long userId, UserRequestDTO.DeleteUserRequestDTO deleteUserRequestDTO);

    TokenResponseDTO.TokenDTO issueTokenAndSetRefreshToken(User user);

    TokenResponseDTO.TokenDTO performAuthentication(String email, String password);

    void setRefreshToken(String refreshToken, User user);

    void checkUserInactive(User user);
}
