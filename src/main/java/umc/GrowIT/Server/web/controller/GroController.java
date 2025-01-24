package umc.GrowIT.Server.web.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.service.GroService.GroCommandService;
import umc.GrowIT.Server.web.controller.specification.GroSpecification;
import umc.GrowIT.Server.web.dto.GroDTO.GroRequestDTO;
import umc.GrowIT.Server.web.dto.GroDTO.GroResponseDTO;

@Tag(name = "Gro", description = "캐릭터 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/characters")
public class GroController implements GroSpecification {
    private final GroCommandService groCommandService;

    public ApiResponse<GroResponseDTO> createGro(@Valid @RequestBody GroRequestDTO request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal(); //사용자 식별 id
        System.out.println(userId);
        String name = request.getName();
        String backgroundItem = request.getBackgroundItem();

        // Service에서 반환된 DTO를 ApiResponse로 감싸서 리턴
        GroResponseDTO responseDTO = groCommandService.createGro(userId, name, backgroundItem);
        return ApiResponse.onSuccess(responseDTO);
    }
}
