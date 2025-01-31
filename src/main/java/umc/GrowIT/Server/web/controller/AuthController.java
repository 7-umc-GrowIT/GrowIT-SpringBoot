package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.AuthType;
import umc.GrowIT.Server.service.authService.AuthService;
import umc.GrowIT.Server.service.userService.UserCommandService;
import umc.GrowIT.Server.service.refreshTokenService.RefreshTokenCommandService;
import umc.GrowIT.Server.web.controller.specification.AuthSpecification;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthRequestDTO;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.KakaoResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

@Slf4j
@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthSpecification {

    private final UserCommandService userCommandService;
    private final AuthService authService;
    private final RefreshTokenCommandService refreshTokenCommandService;

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


    @PostMapping("/reissue")
    public ApiResponse<UserResponseDTO.AccessTokenDTO> reissueToken(@RequestBody @Valid UserRequestDTO.ReissueDTO reissueDTO) {
        UserResponseDTO.AccessTokenDTO accessTokenDTO = refreshTokenCommandService.reissueToken(reissueDTO);
        return ApiResponse.onSuccess(accessTokenDTO);
    }

    @Override
    @PatchMapping("")
    public ApiResponse<UserResponseDTO.DeleteUserResponseDTO> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        UserResponseDTO.DeleteUserResponseDTO deleteUser = userCommandService.delete(userId);

        return ApiResponse.onSuccess(deleteUser);
    }

    @Override
    @PostMapping("/email")
    public ApiResponse<AuthResponseDTO.SendAuthEmailResponseDTO> sendAuthEmail(
            @RequestParam AuthType type,
            @RequestBody @Valid AuthRequestDTO.SendAuthEmailRequestDTO request) {
        AuthResponseDTO.SendAuthEmailResponseDTO result = authService.sendAuthEmail(type, request);

        return ApiResponse.onSuccess(result);
    }

    @Override
    @PostMapping("/verification")
    public ApiResponse<AuthResponseDTO.VerifyAuthCodeResponseDTO> verifyAuthCode(
            @RequestBody @Valid AuthRequestDTO.VerifyAuthCodeRequestDTO request) {
        AuthResponseDTO.VerifyAuthCodeResponseDTO result = authService.verifyAuthCode(request);

        return ApiResponse.onSuccess(result);
    }

    @PostMapping("/login/kakao")
    public ApiResponse<KakaoResponseDTO.KakaoTokenDTO> kakaoLogin(@RequestParam(value = "code") String code) {
        return null;
    }

    @GetMapping("/kakao/test")
    public ApiResponse<KakaoResponseDTO.KakaoTokenDTO> kakaoTest(@RequestParam(value = "code") String code) {
        return null;
    }
}
