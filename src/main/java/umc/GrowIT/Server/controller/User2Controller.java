package umc.GrowIT.Server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.controller.specification.UserSpecification;
import umc.GrowIT.Server.dto.UserRequestDTO;
import umc.GrowIT.Server.dto.UserResponseDTO;
import umc.GrowIT.Server.service.UserCommandService;

@RestController
@RequiredArgsConstructor
public class User2Controller implements UserSpecification {

    private final UserCommandService userCommandService;

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
    public ApiResponse<Void> findPassword(@RequestHeader(name = "tempToken") String tempToken, @RequestBody @Valid UserRequestDTO.PasswordDTO passwordDTO) {
        userCommandService.updatePassword(passwordDTO);
        return ApiResponse.onSuccess();
    }
}
