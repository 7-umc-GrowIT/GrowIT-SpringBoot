package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.AuthType;
import umc.GrowIT.Server.service.authService.AuthService;
import umc.GrowIT.Server.service.oAuthService.OAuthService;
import umc.GrowIT.Server.service.oAuthService.kakaoService.KakaoService;
import umc.GrowIT.Server.service.userService.UserCommandService;
import umc.GrowIT.Server.service.refreshTokenService.RefreshTokenCommandService;
import umc.GrowIT.Server.web.controller.specification.AuthSpecification;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthRequestDTO;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

import static umc.GrowIT.Server.apiPayload.code.status.SuccessStatus.NEED_TO_ACCEPT_TERMS;

@Slf4j
@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController implements AuthSpecification {

    private final UserCommandService userCommandService;
    private final AuthService authService;
    private final RefreshTokenCommandService refreshTokenCommandService;
    private final KakaoService kakaoService;
    private final OAuthService oAuthService;

    @Override
    @PostMapping("/login")
    public ApiResponse<UserResponseDTO.TokenDTO> loginEmail(@RequestBody @Valid UserRequestDTO.EmailLoginDTO emailLoginDTO) {
        UserResponseDTO.TokenDTO tokenDTO = userCommandService.emailLogin(emailLoginDTO);
        return ApiResponse.onSuccess(tokenDTO);
    }

    @Override
    @PostMapping("/signup")
    public ApiResponse<UserResponseDTO.TokenDTO> signupEmail(@RequestBody @Valid UserRequestDTO.UserInfoDTO userInfoDTO) {
        UserResponseDTO.TokenDTO tokenDTO = userCommandService.createUser(userInfoDTO);
        return ApiResponse.onSuccess(tokenDTO);
    }

    @Override
    @PostMapping("/reissue")
    public ApiResponse<UserResponseDTO.AccessTokenDTO> reissueToken(@RequestBody @Valid UserRequestDTO.ReissueDTO reissueDTO) {
        UserResponseDTO.AccessTokenDTO accessTokenDTO = refreshTokenCommandService.reissueToken(reissueDTO);
        return ApiResponse.onSuccess(accessTokenDTO);
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

    @Override
    @PostMapping("/login/kakao")
    public ApiResponse<UserResponseDTO.KakaoLoginDTO> loginKakao(@RequestParam(value = "code") String code) {
        UserResponseDTO.KakaoLoginDTO kakaoLoginDTO = kakaoService.loginKakao(code).block();
        if (kakaoLoginDTO.getSignupRequired())
            return ApiResponse.onSuccess(NEED_TO_ACCEPT_TERMS, kakaoLoginDTO);
        return ApiResponse.onSuccess(kakaoLoginDTO);
    }

    @Override
    @PostMapping("/signup/social")
    public ApiResponse<UserResponseDTO.TokenDTO> signupSocial(@RequestBody @Valid UserRequestDTO.UserTermsDTO userTermsDTO) {
        UserResponseDTO.TokenDTO tokenDTO = oAuthService.signupSocial(userTermsDTO);
        return ApiResponse.onSuccess(tokenDTO);
    }
}
