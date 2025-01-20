package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.domain.enums.AuthType;
import umc.GrowIT.Server.service.authService.AuthService;
import umc.GrowIT.Server.web.controller.specification.AuthSpecification;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthRequestDTO;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;
import umc.GrowIT.Server.service.userService.UserCommandService;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthSpecification {

    private final UserCommandService userCommandService;
    private final AuthService authService;

    @PostMapping("/login/email")
    public ApiResponse<UserResponseDTO.TokenDTO> loginEmail(@RequestBody @Valid UserRequestDTO.EmailLoginDTO emailLoginDTO) {
        UserResponseDTO.TokenDTO tokenDTO = userCommandService.emailLogin(emailLoginDTO);
        return ApiResponse.onSuccess(tokenDTO);
    }

    @PostMapping("/users")
    public ApiResponse<UserResponseDTO.TokenDTO> createUser(@RequestBody @Valid UserRequestDTO.UserInfoDTO userInfoDTO) {
        UserResponseDTO.TokenDTO tokenDTO = userCommandService.createUser(userInfoDTO);
        if (tokenDTO == null) {
            return ApiResponse.onFailure(ErrorStatus.EMAIL_ALREADY_EXISTS.getCode(), ErrorStatus.EMAIL_ALREADY_EXISTS.getMessage(), null);
        }
        return ApiResponse.onSuccess(tokenDTO);
    }

    @PatchMapping("/users/password/find")
    public ApiResponse<Void> findPassword(@RequestBody @Valid UserRequestDTO.PasswordDTO passwordDTO) {
        userCommandService.updatePassword(passwordDTO);
        return ApiResponse.onSuccess();
    }

    @Override
    @PatchMapping("")
    public ApiResponse<UserResponseDTO.DeleteUserResponseDTO> deleteUser() {
        // 임시로 사용자 ID 지정
        Long userId = 16L;

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
}
