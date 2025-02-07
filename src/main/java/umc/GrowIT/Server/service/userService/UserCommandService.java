package umc.GrowIT.Server.service.userService;

import umc.GrowIT.Server.domain.CustomUserDetails;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserTerm;
import umc.GrowIT.Server.web.dto.TermDTO.TermRequestDTO;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

import java.util.List;


public interface UserCommandService {

    TokenResponseDTO.TokenDTO signupEmail(UserRequestDTO.UserInfoDTO userInfoDTO);

    TokenResponseDTO.TokenDTO loginEmail(UserRequestDTO.EmailLoginDTO emailLoginDTO);

    void updatePassword(UserRequestDTO.PasswordDTO passwordDTO);

    UserResponseDTO.DeleteUserResponseDTO delete(Long userId);

    TokenResponseDTO.TokenDTO issueTokenAndSetRefreshToken(User user);

    TokenResponseDTO.TokenDTO performAuthentication(String email, String password);

    void setRefreshToken(String refreshToken, User user);
}
