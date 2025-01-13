package umc.GrowIT.Server.web.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

@Tag(name = "User", description = "User 관련 API")
@RestController
@RequestMapping("/users")
public class UserRestController {

    // 사용자 계정 관련
    @PostMapping("/password")
    @Operation(
            summary = "비밀번호 재설정 API",
            description = "사용자가 비밀번호를 재설정하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<UserRequestDTO.ResetPasswordRequestDTO> resetPassword(@RequestBody UserRequestDTO.ResetPasswordRequestDTO request) {
        return ApiResponse.onSuccess(null);
    }

    @PatchMapping("")
    @Operation(
            summary = "회원 탈퇴 API",
            description = "사용자가 자신의 계정을 삭제하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<UserResponseDTO.DeleteUserResponseDTO> deleteUser(@RequestBody UserRequestDTO.DeleteUserRequestDTO request) {
        return ApiResponse.onSuccess(null);
    }

    // 사용자 인증 관련
    @PostMapping("/email")
    @Operation(
            summary = "인증 메일 전송 API",
            description = "사용자에게 인증 메일을 전송하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<UserResponseDTO.SendAuthEmailResponseDTO> sendAuthEmail(@RequestBody UserRequestDTO.SendAuthEmailRequestDTO request) {
        return ApiResponse.onSuccess(null);
    }

    @PostMapping("/verification")
    @Operation(
            summary = "인증 번호 확인 API",
            description = "사용자가 입력한 인증 번호를 확인하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<UserResponseDTO.VerifyAuthCodeResponseDTO> verifyAuthCode(@RequestBody UserRequestDTO.VerifyAuthCodeRequestDTO request) {
        return ApiResponse.onSuccess(null);
    }
}
