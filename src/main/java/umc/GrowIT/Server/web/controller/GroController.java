package umc.GrowIT.Server.web.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.service.GroService.GroCommandService;
import umc.GrowIT.Server.service.GroService.GroQueryService;
import umc.GrowIT.Server.web.controller.specification.GroSpecification;
import umc.GrowIT.Server.web.dto.GroDTO.GroRequestDTO;
import umc.GrowIT.Server.web.dto.GroDTO.GroResponseDTO;

@Tag(name = "Gro", description = "캐릭터 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/characters")
public class GroController implements GroSpecification {
    private final GroCommandService groCommandService;
    private final GroQueryService groQueryService;

    public ApiResponse<GroResponseDTO.CreateResponseDTO> createGro(@Valid @RequestBody GroRequestDTO.CreateRequestDTO request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal(); //사용자 식별 id
        System.out.println(userId);
        String name = request.getName();
        String backgroundItem = request.getBackgroundItem();

        // Service에서 반환된 DTO를 ApiResponse로 감싸서 리턴
        GroResponseDTO.CreateResponseDTO responseDTO = groCommandService.createGro(userId, name, backgroundItem);
        return ApiResponse.onSuccess(responseDTO);
    }

    @Override
    @GetMapping("")
    public ApiResponse<GroResponseDTO.GroAndEquippedItemsDTO> getGroAndEquippedItems() {
        //AccessToken에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        GroResponseDTO.GroAndEquippedItemsDTO result = groQueryService.getGroAndEquippedItems(userId);

        return ApiResponse.onSuccess(result);
    }

    @Override
    @PatchMapping("")
    public ApiResponse<Void> updateNickname(@Valid @RequestBody GroRequestDTO.NicknameRequestDTO nicknameDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        groCommandService.updateNickname(userId, nicknameDTO.getName());
        return ApiResponse.onSuccess();
    }

}
