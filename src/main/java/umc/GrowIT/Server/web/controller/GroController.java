package umc.GrowIT.Server.web.controller;


import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.service.groService.GroCommandService;
import umc.GrowIT.Server.service.groService.GroQueryService;
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

    @Override
    public ApiResponse<GroResponseDTO.CreateResponseDTO> createGro(@AuthenticationPrincipal Long userId, @Valid @RequestBody GroRequestDTO.CreateRequestDTO request) {
        System.out.println(userId);
        String name = request.getName();
        String backgroundItem = request.getBackgroundItem();

        // Service에서 반환된 DTO를 ApiResponse로 감싸서 리턴
        GroResponseDTO.CreateResponseDTO responseDTO = groCommandService.createGro(userId, name, backgroundItem);
        return ApiResponse.onSuccess(responseDTO);
    }

    @Override
    @GetMapping("")
    public ApiResponse<GroResponseDTO.GroAndEquippedItemsDTO> getGroAndEquippedItems(@AuthenticationPrincipal Long userId) {
        GroResponseDTO.GroAndEquippedItemsDTO result = groQueryService.getGroAndEquippedItems(userId);

        return ApiResponse.onSuccess(result);
    }

    @Override
    @PatchMapping("/nickname")
    public ApiResponse<Void> updateNickname(@AuthenticationPrincipal Long userId, @Valid @RequestBody GroRequestDTO.NicknameRequestDTO nicknameDTO) {
        groCommandService.updateNickname(userId, nicknameDTO.getName());
        return ApiResponse.onSuccess();
    }

}
