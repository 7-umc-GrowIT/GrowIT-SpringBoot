package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.service.withdrawalService.WithdrawalQueryService;
import umc.GrowIT.Server.web.controller.specification.WithdrawalSpecification;
import umc.GrowIT.Server.web.dto.WithdrawalDTO.WithdrawalResponseDTO;

import java.util.List;

@Tag(name = "Withdrawal", description = "탈퇴 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/withdrawal")
public class WithdrawalController implements WithdrawalSpecification {

    private final WithdrawalQueryService withdrawalQueryService;

    @Override
    @GetMapping("/reasons")
    public ApiResponse<List<WithdrawalResponseDTO.WithdrawalReasonDTO>> getWithdrawalReasons() {
        return ApiResponse.onSuccess(withdrawalQueryService.getWithdrawalReasons());
    }
}
