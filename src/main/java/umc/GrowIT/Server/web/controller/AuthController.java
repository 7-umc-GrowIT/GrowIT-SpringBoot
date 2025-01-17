package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;
import umc.GrowIT.Server.jwt.JwtTokenUtil;
import umc.GrowIT.Server.web.controller.specification.AuthSpecification;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;
import umc.GrowIT.Server.service.authService.UserCommandService;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthSpecification {

    private final UserCommandService userCommandService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil JwtTokenUtil;

    @PostMapping("/login/email")
    public ApiResponse<UserResponseDTO.TokenDTO> loginEmail(@RequestBody @Valid UserRequestDTO.EmailLoginDTO emailLoginDTO) {
        UserResponseDTO.TokenDTO tokenDTO = userCommandService.emailLogin(emailLoginDTO);
        return ApiResponse.onSuccess(tokenDTO);
    }

    @PostMapping("/users")
    public ApiResponse<UserResponseDTO.TokenDTO> createUser(@RequestBody @Valid UserRequestDTO.UserInfoDTO userInfoDTO) {
        UserResponseDTO.TokenDTO tokenDTO = userCommandService.createUser(userInfoDTO);
        return ApiResponse.onSuccess(tokenDTO);
    }

    @PatchMapping("/users/password/find")
    public ApiResponse<Void> findPassword(@RequestBody @Valid UserRequestDTO.PasswordDTO passwordDTO) {
        userCommandService.updatePassword(passwordDTO);
        return ApiResponse.onSuccess();
    }
}
