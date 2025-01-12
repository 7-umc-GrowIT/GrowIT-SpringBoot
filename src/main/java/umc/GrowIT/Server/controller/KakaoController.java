package umc.GrowIT.Server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.controller.specification.KakaoSpecification;
import umc.GrowIT.Server.dto.KakaoResponseDTO;
import umc.GrowIT.Server.service.KakaoService;

@RestController
@RequiredArgsConstructor
public class KakaoController implements KakaoSpecification {

    private final KakaoService kakaoService;

    @GetMapping("/login/kakao")
    public ApiResponse<KakaoResponseDTO.KakaoTokenDTO> kakaoLogin(@RequestParam(value = "code", required = false) String code) {
        KakaoResponseDTO.KakaoTokenDTO token = kakaoService.getToken(code);
        return ApiResponse.onSuccess(token);
    }
}
