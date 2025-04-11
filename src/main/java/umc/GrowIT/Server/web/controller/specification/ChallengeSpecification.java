package umc.GrowIT.Server.web.controller.specification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.util.List;

public interface ChallengeSpecification {

    @GetMapping("summary")
    @Operation(summary = "챌린지 홈 조회 API", description = "사용자의 챌린지 홈 화면에 보여질 챌린지 요약 정보를 조회하는 API입니다. <br> " +
            "오늘의 챌린지 추천과 그로의 챌린지 리포트를 조회할 수 있습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ChallengeResponseDTO.ChallengeHomeDTO> getChallengeHome();

    @GetMapping("/")
    @Operation(summary = "챌린지 현황 조회 API", description = "챌린지의 진행 상태(미완료/완료 등)를 조회하는 API입니다. <br> " +
            "dtype에 값을 주지않으면 전체 완료/미완료 챌린지를 조회할 수 있습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<Page<ChallengeResponseDTO.ChallengeStatusDTO>> getChallengeStatus(
            @RequestParam(required = false) UserChallengeType dtype,
            @RequestParam(required = false) Boolean completed,
            @RequestParam Integer page);

    @PostMapping("{challengeId}/select")
    @Operation(summary = "선택된 챌린지 저장 API", description = "사용자가 선택한 챌린지를 저장하는 API입니다. <br> " +
            "DAILY와 RANDOM에 각각 저장하고 싶은 챌린지 아이디를 입력하여 리스트 형태로 보내주세요. <br> " +
            "데일리챌린지는 최대 2개까지 저장할 수 있으며, 랜덤챌린지는 최대 1개까지 저장할 수 있습니다. <br> " +
            "ex) [\n" +
            "  {\n" +
            "    \"challengeIds\": [\n" +
            "      1,2\n" +
            "    ],\n" +
            "    \"dtype\": \"DAILY\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"challengeIds\": [\n" +
            "      3\n" +
            "    ],\n" +
            "    \"dtype\": \"RANDOM\"\n" +
            "  }\n" +
            "]"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4002", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4001", description = "❌ 챌린지를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4006", description = "❌ 최소 하나의 챌린지를 선택해야 합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4007", description = "❌ 데일리 챌린지는 최대 2개까지 저장 가능합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4008", description = "❌ 랜덤 챌린지는 최대 1개만 저장 가능합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ChallengeResponseDTO.SelectChallengeDTO> selectChallenges(@RequestBody List<ChallengeRequestDTO.SelectChallengeRequestDTO> selectRequestList);

    @PostMapping("{userChallengeId}/prove")
    @Operation(summary = "챌린지 인증 작성 API", description = "챌린지 인증을 작성하는 API입니다. <br>" +
            "s3에 업로드한 객체 URL을 certificationImageUrl로 넘겨주세요. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4002", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC4001", description = "❌ 사용자 챌린지를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4004", description = "❌ 이미 완료된 챌린지입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ChallengeResponseDTO.ProofDetailsDTO> createChallengeProof(@PathVariable Long userChallengeId, @RequestBody ChallengeRequestDTO.ProofRequestDTO proofRequest);

    @GetMapping("{userChallengeId}")
    @Operation(summary = "챌린지 인증 내역 조회 API", description = "사용자 챌린지 ID를  path variable로 전달받아 해당 사용자 챌린지의 인증 내역을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC4001", description = "❌ 사용자 챌린지를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4002", description = "❌ 챌린지 인증이 완료되지 않았습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ChallengeResponseDTO.ProofDetailsDTO> getChallengeProofDetails(@PathVariable Long userChallengeId);


    @PatchMapping("{userChallengeId}")
    @Operation(summary = "챌린지 인증 수정 API", description = "챌린지 인증을 수정하는 API입니다. <br>" +
            "s3에 업로드한 객체 URL을 certificationImageUrl로 넘겨주세요. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4002", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC4001", description = "❌ 사용자 챌린지를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4002", description = "❌ 챌린지 인증이 완료되지 않았습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })

    ApiResponse<ChallengeResponseDTO.ModifyProofDTO> updateChallengeProof(@PathVariable Long userChallengeId, @RequestBody(required = false) ChallengeRequestDTO.ProofRequestDTO updateRequest);


    @DeleteMapping("{userChallengeId}")
    @Operation(
            summary = "사용자 챌린지 삭제 API",
            description = "사용자의 특정 챌린지를 삭제하는 API입니다.<br>" +
                    "사용자 챌린지 ID를 path variable로 전달받아 해당 사용자 챌린지를 삭제합니다.<br>" +
                    "❗Request Header에 JWT Access Token 값을 넣어야 합니다.❗"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4002", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC4001", description = "❌ 사용자 챌린지가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC4002", description = "❌ 완료된 챌린지는 삭제가 불가합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))

    })
    @Parameter(name = "userChallengeId", description = "삭제할 사용자 챌린지의 ID", required = true)
    ApiResponse<ChallengeResponseDTO.DeleteChallengeResponseDTO> deleteChallenge(@PathVariable("userChallengeId") Long userChallengeId);
}
