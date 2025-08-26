package umc.GrowIT.Server.web.controller.specification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.util.List;
import java.util.Map;

public interface ChallengeSpecification {

    @GetMapping("summary")
    @Operation(summary = "챌린지 홈 조회 API", description = "사용자의 챌린지 홈 화면에 보여질 챌린지 요약 정보를 조회하는 API입니다. <br> " +
            "오늘의 챌린지 추천과 그로의 챌린지 리포트를 조회할 수 있습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ChallengeResponseDTO.ChallengeHomeDTO> getChallengeHome();

    @GetMapping("")
    @Operation(summary = "챌린지 현황 조회 API", description = "챌린지의 진행 상태(미완료/완료 등)를 조회하는 API입니다. <br> " +
            "dtype에 값을 주지 않으면 전체 완료/미완료 챌린지를 조회할 수 있습니다. <br> " +
            "응답은 슬라이스 기반으로 페이징되며, 현재 페이지, 리스트, 다음 페이지 존재 여부 등의 정보가 포함됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ChallengeResponseDTO.ChallengeStatusPagedResponseDTO> getChallengeStatus(
            @RequestParam(required = false) UserChallengeType dtype,
            @RequestParam(required = false) Boolean completed,
            @RequestParam Integer page);

    @PostMapping("select")
    @Operation(summary = "선택한 챌린지 저장 API", description = "사용자가 선택한 챌린지를 저장하는 API입니다. <br> " +
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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4004", description = "❌ 최소 하나의 챌린지를 선택해야 합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4005", description = "❌ 데일리 챌린지는 최대 2개까지 저장 가능합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE4006", description = "❌ 랜덤 챌린지는 최대 1개만 저장 가능합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<Void> selectChallenges(@RequestBody List<ChallengeRequestDTO.SelectChallengeRequestDTO> selectRequestList);

    @PostMapping("presigned-url")
    @Operation(summary = "챌린지 인증 이미지 업로드용 presigned url 생성 API", description = "챌린지 인증 이미지를 S3에 직접 업로드할 수 있는 presigned url을 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "S34001", description = "❌ 파일 확장자가 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    ApiResponse<ChallengeResponseDTO.ProofPresignedUrlResponseDTO> getProofPresignedUrl(@Valid @RequestBody ChallengeRequestDTO.ProofRequestPresignedUrlDTO request);

    @PostMapping("{userChallengeId}")
    @Operation(summary = "챌린지 인증 작성 API", description = "챌린지 인증을 작성하는 API입니다. <br>" +
            "presigned url 생성 시 반환된 파일명을 certificationImage로 넘겨주세요. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4002", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC4001", description = "❌ 사용자 챌린지를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC4003", description = "❌ 이미 완료된 챌린지입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<Void> createChallengeProof(@PathVariable Long userChallengeId, @Valid @RequestBody ChallengeRequestDTO.ProofRequestDTO proofRequest);

    @GetMapping("{userChallengeId}")
    @Operation(summary = "챌린지 인증 내역 조회 API", description = "사용자 챌린지 ID를  path variable로 전달받아 해당 사용자 챌린지의 인증 내역을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC4001", description = "❌ 사용자 챌린지를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC4004", description = "❌ 챌린지 인증이 완료되지 않았습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ChallengeResponseDTO.ProofDetailsDTO> getChallengeProofDetails(@PathVariable Long userChallengeId);


    @PatchMapping("{userChallengeId}")
    @Operation(summary = "챌린지 인증 수정 API", description = "챌린지 인증을 수정하는 API입니다. <br>" +
            "s3에 업로드한 객체 URL을 certificationImageUrl로 넘겨주세요. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4002", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC4001", description = "❌ 사용자 챌린지를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC4004", description = "❌ 챌린지 인증이 완료되지 않았습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC4005", description = "❌ 챌린지 인증 내역에 수정사항이 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })

    ApiResponse<Void> updateChallengeProof(@PathVariable Long userChallengeId, @Valid @RequestBody(required = false) ChallengeRequestDTO.ProofRequestDTO updateRequest);


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
