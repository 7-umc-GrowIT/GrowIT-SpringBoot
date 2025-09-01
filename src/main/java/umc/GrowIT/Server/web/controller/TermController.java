package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.Term;
import umc.GrowIT.Server.web.controller.specification.TermSpecification;
import umc.GrowIT.Server.web.dto.TermDTO.TermResponseDTO;
import umc.GrowIT.Server.service.termService.TermQueryService;

import java.util.List;

@Tag(name = "Term", description = "약관 동의 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/terms")
public class TermController implements TermSpecification {

    private final TermQueryService termQueryService;

    @Override
    @GetMapping("")
    public ApiResponse<List<TermResponseDTO.TermDTO>> getTerms() {
        return ApiResponse.onSuccess(termQueryService.getTerms());
    }
}
