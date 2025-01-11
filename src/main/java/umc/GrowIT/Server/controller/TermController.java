package umc.GrowIT.Server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.controller.specification.TermSpecification;
import umc.GrowIT.Server.dto.TermResponseDTO;
import umc.GrowIT.Server.service.TermQueryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TermController implements TermSpecification {

    private final TermQueryService termQueryService;

    @GetMapping("/terms")
    public ApiResponse<List<TermResponseDTO.TermDTO>> getTerms() {
        return ApiResponse.onSuccess(termQueryService.getTerms());
    }

}
