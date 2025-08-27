package umc.GrowIT.Server.web.controller;

import static umc.GrowIT.Server.apiPayload.code.status.SuccessStatus.NEED_TO_ACCEPT_TERMS;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.AuthType;
import umc.GrowIT.Server.service.authService.AuthService;
import umc.GrowIT.Server.service.oAuthService.OAuthCommandService;
import umc.GrowIT.Server.service.oAuthService.appleService.AppleService;
import umc.GrowIT.Server.service.oAuthService.kakaoService.KakaoService;
import umc.GrowIT.Server.service.refreshTokenService.RefreshTokenCommandService;
import umc.GrowIT.Server.service.userService.UserCommandService;
import umc.GrowIT.Server.web.controller.specification.AuthSpecification;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthRequestDTO;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthRequestDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthResponseDTO;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenRequestDTO;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController implements AuthSpecification {
    private final UserCommandService userCommandService;
    private final AuthService authService;
    private final RefreshTokenCommandService refreshTokenCommandService;
    private final KakaoService kakaoService;
    private final AppleService appleService;
    private final OAuthCommandService oAuthService;

    @Override
    @PostMapping("/login")
    public ApiResponse<TokenResponseDTO.TokenDTO> loginEmail(@RequestBody @Valid UserRequestDTO.EmailLoginDTO emailLoginDTO) {
        TokenResponseDTO.TokenDTO tokenDTO = userCommandService.loginEmail(emailLoginDTO);
        return ApiResponse.onSuccess(tokenDTO);
    }

    @Override
    @PostMapping("/signup")
    public ApiResponse<TokenResponseDTO.TokenDTO> signupEmail(@RequestBody @Valid UserRequestDTO.UserInfoDTO userInfoDTO) {
        TokenResponseDTO.TokenDTO tokenDTO = userCommandService.signupEmail(userInfoDTO);
        return ApiResponse.onSuccess(tokenDTO);
    }

    @Override
    @PostMapping("/reissue")
    public ApiResponse<TokenResponseDTO.AccessTokenDTO> reissueToken(@RequestBody @Valid TokenRequestDTO.ReissueDTO reissueDTO) {
        TokenResponseDTO.AccessTokenDTO accessTokenDTO = refreshTokenCommandService.reissueToken(reissueDTO);
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
    public ApiResponse<OAuthResponseDTO.OAuthLoginDTO> loginKakao(@RequestBody @Valid OAuthRequestDTO.SocialLoginDTO socialLoginDTO) {
        OAuthResponseDTO.OAuthLoginDTO kakaoLoginDTO = kakaoService.loginKakao(socialLoginDTO);
        if (kakaoLoginDTO.getSignupRequired())
            return ApiResponse.onSuccess(NEED_TO_ACCEPT_TERMS, kakaoLoginDTO);
        return ApiResponse.onSuccess(kakaoLoginDTO);
    }

    @Override
    @PostMapping("/login/apple")
    public ApiResponse<OAuthResponseDTO.OAuthLoginDTO> loginApple(@RequestBody @Valid OAuthRequestDTO.SocialLoginDTO socialLoginDTO) {
        OAuthResponseDTO.OAuthLoginDTO appleTokenResponseDTO = appleService.socialLogin(socialLoginDTO);
        return ApiResponse.onSuccess(appleTokenResponseDTO);
    }

    @Override
    @PostMapping("/signup/social")
    public ApiResponse<TokenResponseDTO.TokenDTO> signupSocial(@RequestBody @Valid OAuthRequestDTO.OAuthUserInfoAndUserTermsDTO oAuthUserInfoAndUserTermsDTO) {
        TokenResponseDTO.TokenDTO tokenDTO = oAuthService.signupSocial(oAuthUserInfoAndUserTermsDTO);
        return ApiResponse.onSuccess(tokenDTO);
    }

    @Override
    @PostMapping("/logout")
    public ApiResponse<AuthResponseDTO.LogoutResponseDTO> logout() {
        // AccessToken에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        // 로그아웃 처리
        AuthResponseDTO.LogoutResponseDTO result = authService.logout(userId);

        return ApiResponse.onSuccess(result);
    }

}
