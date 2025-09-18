package umc.GrowIT.Server.web.controller.specification;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.web.dto.GroDTO.GroRequestDTO;
import umc.GrowIT.Server.web.dto.GroDTO.GroResponseDTO;

public interface GroSpecification {

    @PostMapping("")
    @Operation(summary = "그로 캐릭터 생성", description = "사용자의 그로 캐릭터를 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "GRO_409_02", description = "❌ 다른 닉네임과 중복되는 닉네임입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400", description = "❌ 닉네임은 2~8자 이내로 작성해야 합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<GroResponseDTO.CreateResponseDTO> createGro(@AuthenticationPrincipal Long userId, @Valid @RequestBody GroRequestDTO.CreateRequestDTO request);

    @GetMapping("")
    @Operation(
            summary = "그로와 착용 아이템 이미지 조회 API",
            description = "그로와 그로가 착용 중인 아이템들에 대한 이미지들을 조회하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_401_01", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "GRO_404_01", description = "❌ 그로에 대한 정보가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "GRO_500_01", description = "❌ 그로 레벨이 유효하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UI_404_01", description = "❌ 착용 중인 사용자 아이템이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<GroResponseDTO.GroAndEquippedItemsDTO> getGroAndEquippedItems(@AuthenticationPrincipal Long userId);

    @PatchMapping("/nickname")
    @Operation(summary = "그로 닉네임 변경", description = "그로의 닉네임을 변경합니다. 닉네임을 2~8자 사이로 입력해주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400", description = "❌ 닉네임은 2~8자 이내로 작성해야 합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "GRO_409_02", description = "❌ 다른 닉네임과 중복되는 닉네임입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "GRO_400_01", description = "❌ 그로의 닉네임에 수정사항이 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<Void> updateNickname(@AuthenticationPrincipal Long userId, @Valid @RequestBody GroRequestDTO.NicknameRequestDTO request);
}
