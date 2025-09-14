package umc.GrowIT.Server.web.controller.specification;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.AuthType;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthRequestDTO;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthRequestDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthResponseDTO;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenRequestDTO;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;

public interface AuthSpecification {

    @PostMapping("/signup")
    @SecurityRequirement(name = "")
    @Operation(summary = "이메일 회원가입 API", description = "GET /terms 약관 목록 조회 API 에서 조회된 약관에 대한 동의 정보를 함께 주세요.", security = @SecurityRequirement(name = ""))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400", description = "❌ 회원가입 입력 형식이 맞지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_400_01", description = "❌ 이메일 인증을 완료해주세요.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_409_03", description = "❌ 이미 존재하는 이메일입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TERM_400_01", description = "❌ 필수 약관에 반드시 동의해야 합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TERM_400_02", description = "❌ 전체 약관 정보를 주세요.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TERM_404_01", description = "❌ 존재하지 않는 약관을 요청했습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<AuthResponseDTO.LoginResponseDTO> signupEmail(@RequestBody @Valid UserRequestDTO.UserInfoDTO userInfoDTO);

    @PostMapping("/login")
    @Operation(summary = "이메일 로그인 API", description = "이메일 로그인 API 입니다. AccessToken, RefreshToken 이 모두 만료되어 토큰 재발급 API 에서 AccessToken 재발급이 불가능하면 로그인 화면으로 이동합니다.", security = @SecurityRequirement(name = ""))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400", description = "❌ 로그인 입력 형식이 맞지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_401_01", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_403_01", description = "❌ 탈퇴한 회원입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<AuthResponseDTO.LoginResponseDTO> loginEmail(@RequestBody @Valid UserRequestDTO.EmailLoginDTO emailLoginDTO);

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급 API", description = "AccessToken 이 만료되었다는 401 에러 발생 시 토큰 재발급을 요청하는 API 입니다. 로그인 또는 회원가입 시 얻은 RefreshToken 을 Request Body 에 담아 요청해 주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_401_02", description = "❌ 만료된 토큰입니다. 토큰의 만료 시간이 지나 더 이상 유효하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_403_01", description = "❌ 탈퇴한 회원입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_404_01", description = "❌ 데이터베이스에서 refreshToken을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<TokenResponseDTO.AccessTokenDTO> reissueToken(@RequestBody @Valid TokenRequestDTO.ReissueDTO reissueDTO);

    @PostMapping("/email/send")
    @Operation(summary = "인증 이메일 발송 API", description = "사용자가 회원가입 또는 비밀번호 재설정을 위해 입력한 이메일로 인증번호를 발송하는 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_409_01", description = "❌ 이미 일반 회원가입된 이메일입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_409_02", description = "❌ 이미 소셜 회원가입된 이메일입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_404_02", description = "❌ 회원가입되지 않은 이메일입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_400_05", description = "❌ 소셜 회원가입 계정은 비밀번호 재설정이 불가능합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_400_02", description = "❌ 이메일 인증 타입이 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<AuthResponseDTO.SendAuthEmailResponseDTO> sendAuthEmail(
            @Parameter(description = "인증 메일 유형 (SIGNUP: 회원가입, PASSWORD_RESET: 비밀번호 재설정)", example = "SIGNUP")
            @RequestParam AuthType type,
            @RequestBody @Valid AuthRequestDTO.SendAuthEmailRequestDTO request
    );

    @PostMapping("/email/verify")
    @Operation(summary = "인증번호 검증 API", description = "사용자가 입력한 인증번호가 발송된 코드와 일치하는지 검증하는 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_400_03", description = "❌ 유효한 인증번호가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_400_04", description = "❌ 인증번호가 올바르지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<Void> verifyAuthCode(@RequestBody @Valid AuthRequestDTO.VerifyAuthCodeRequestDTO request);

    @PostMapping("/login/kakao")
    @Operation(summary = "카카오 소셜 로그인", description = "카카오 소셜 로그인 API 입니다.", security = @SecurityRequirement(name = ""))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_400_05", description = "❌ 유효하지 않은 인가 코드입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_403_01", description = "❌ 탈퇴한 회원입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<OAuthResponseDTO.OAuthLoginDTO> loginKakao(OAuthRequestDTO.SocialLoginDTO socialLoginDTO);

    @PostMapping("/login/apple")
    @Operation(summary = "애플 소셜 로그인", description = "애플 소셜 로그인 API 입니다.", security = @SecurityRequirement(name = ""))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_400_05", description = "❌ 유효하지 않은 인가 코드입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_403_01", description = "❌ 탈퇴한 회원입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<OAuthResponseDTO.OAuthLoginDTO> loginApple(OAuthRequestDTO.SocialLoginDTO socialLoginDTO);

    @PostMapping("/signup/social")
    @Operation(summary = "소셜 간편 가입", description = "소셜 로그인 시 계정이 존재하지 않을 때 약관 목록으로 간편 가입을 진행하는 API 입니다. (termId 1~4는 필수 동의 항목입니다.)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OAUTH_400_01", description = "❌ 요청한 이메일로 가입할 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OAUTH_409_01", description = "❌ 이미 가입한 소셜 계정입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TERM_400_01", description = "❌ 필수 약관에 반드시 동의해야 합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TERM_400_02", description = "❌ 전체 약관 정보를 주세요.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TERM_404_01", description = "❌ 존재하지 않는 약관을 요청했습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<AuthResponseDTO.LoginResponseDTO> signupSocial(@RequestBody @Valid OAuthRequestDTO.OAuthUserInfoAndUserTermsDTO oAuthUserInfoAndUserTermsDTO);

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "사용자 로그아웃을 처리하는 API입니다. AccessToken을 무효화하고 RefreshToken을 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_401_01", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_403_01", description = "❌ 탈퇴한 회원입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_401_01", description = "❌ 유효하지 않은 토큰입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_401_02", description = "❌ 만료된 토큰입니다. 토큰의 만료 시간이 지나 더 이상 유효하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_401_05", description = "❌ 토큰이 제공되지 않았습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<AuthResponseDTO.LogoutResponseDTO> logout();
}
