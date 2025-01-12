package umc.GrowIT.Server.controller.specification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.dto.UserRequestDTO;
import umc.GrowIT.Server.dto.UserResponseDTO;

public interface UserSpecification {

    @PostMapping("/users")
    @SecurityRequirement(name = "")
    @Operation(summary = "이메일 회원가입", description = "", security = @SecurityRequirement(name = ""))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4003", description = "❌ 이미 존재하는 이메일입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TEMP4001", description = "❌ 존재하지 않는 약관을 요청했습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TEMP4002", description = "❌ 필수 약관에 반드시 동의해야 합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TEMP4003", description = "❌ 전체 약관 정보를 주세요.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "❌ 회원가입 입력 형식이 맞지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<UserResponseDTO.TokenDTO> createUser(@RequestBody @Valid UserRequestDTO.UserInfoDTO userInfoDTO);


    @PostMapping("/login/email")
    @Operation(summary = "이메일 로그인", description = "", security = @SecurityRequirement(name = ""))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4002", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4004", description = "❌ 탈퇴한 회원입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "❌ 로그인 입력 형식이 맞지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<UserResponseDTO.TokenDTO> loginEmail(@RequestBody @Valid UserRequestDTO.EmailLoginDTO emailLoginDTO);


    @PatchMapping("/users/password/find")
    @Operation(summary = "비밀번호 변경", description = "", security = @SecurityRequirement(name = "tempToken"))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH4002", description = "❌ 이메일 인증을 완료해주세요.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4002", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4004", description = "❌ 탈퇴한 회원입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PWD4001", description = "❌ 비밀번호 확인이 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<Void> findPassword(@RequestBody @Valid UserRequestDTO.PasswordDTO passwordDTO);
}
