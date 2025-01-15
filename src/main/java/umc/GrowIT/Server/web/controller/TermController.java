package umc.GrowIT.Server.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.web.controller.specification.TermSpecification;
import umc.GrowIT.Server.web.dto.TermDTO.TermResponseDTO;
import umc.GrowIT.Server.service.termService.TermQueryService;

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
