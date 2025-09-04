package umc.GrowIT.Server.web.controller.specification;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

public interface ChallengeSpecification {

    @GetMapping("")
    @Operation(summary = "챌린지 홈 조회 API", description = "사용자의 챌린지 홈 화면에 보여질 챌린지 요약 정보를 조회하는 API입니다. <br> " +
            "오늘의 챌린지 추천과 그로의 챌린지 리포트를 조회할 수 있습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ChallengeResponseDTO.ChallengeHomeDTO> getChallengeHome();

    @GetMapping("status")
    @Operation(summary = "챌린지 현황 조회 API", description = "챌린지의 진행 상태(미완료/완료 등)를 조회하는 API입니다. <br> " +
            "challengeType에 값을 주지 않으면 전체 완료/미완료 챌린지를 조회할 수 있습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ChallengeResponseDTO.ChallengeStatusPagedResponseDTO> getChallengeStatus(
            @Parameter(description = "챌린지 유형",
                    example = "DAILY")
            @RequestParam(required = false) UserChallengeType challengeType,
            @Parameter(description = "챌린지 완료(인증) 여부",
                    schema = @Schema(allowableValues = {"true", "false"}),
                    example = "false")
            @RequestParam(required = false) Boolean completed,
            @Parameter(description = "조회할 페이지",
                    example = "1")
            @RequestParam Integer page);

    @PostMapping("select")
    @Operation(summary = "선택한 챌린지 저장 API", description = "일기 분석 결과의 챌린지 목록에서 사용자가 선택한 챌린지를 저장하는 API입니다. <br> " +
            "DAILY와 RANDOM에 각각 저장하고 싶은 챌린지 아이디를 입력하여 리스트 형태로 보내주세요. <br> " +
            "데일리챌린지는 최대 2개까지 저장할 수 있으며, 랜덤챌린지는 최대 1개까지 저장할 수 있습니다. <br> ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_401_01", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE_400_01", description = "❌ 챌린지는 3개까지 저장 가능합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE_400_02", description = "❌ 최소 하나의 챌린지를 선택해야 합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE_400_03", description = "❌ 데일리 챌린지는 최대 2개까지 저장 가능합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE_400_04", description = "❌ 랜덤 챌린지는 최대 1개만 저장 가능합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE_404_01", description = "❌ 챌린지를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ChallengeResponseDTO.SelectChallengeResponseDTO> selectChallenges(@RequestBody List<ChallengeRequestDTO.SelectChallengeRequestDTO> selectRequestList);

    @PostMapping("presigned-url")
    @Operation(summary = "사용자 챌린지 인증 이미지 업로드용 presigned url 생성 API", description = "사용자 챌린지 인증 이미지를 S3에 직접 업로드할 수 있는 presigned url을 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "S3_400_01", description = "❌ 파일 확장자가 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ChallengeResponseDTO.ProofPresignedUrlResponseDTO> getProofPresignedUrl(@Valid @RequestBody ChallengeRequestDTO.ProofRequestPresignedUrlDTO request);

    @PostMapping("{userChallengeId}")
    @Operation(summary = "사용자 챌린지 인증 작성 API", description = "사용자의 특정 챌린지 인증내역을 작성하는 API입니다. <br>" +
            "미완료 상태의 챌린지만 인증 작성할 수 있습니다. <br>" +
            "presigned url 생성 시 반환된 파일명을 certificationImageName으로 넘겨주세요. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_401_01", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC_400_03", description = "❌ 하루에 10번만 인증이 가능합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC_404_01", description = "❌ 사용자 챌린지가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC_409_02", description = "❌ 이미 완료된 챌린지입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameter(name = "userChallengeId", description = "인증 작성할 사용자 챌린지의 ID", example = "1")
    ApiResponse<ChallengeResponseDTO.CreateProofDTO> createChallengeProof(@PathVariable Long userChallengeId,
            @Valid @RequestBody ChallengeRequestDTO.ProofRequestDTO proofRequest);

    @GetMapping("{userChallengeId}")
    @Operation(summary = "사용자 챌린지 인증 내역 조회 API", description = "사용자의 특정 챌린지 인증 내역을 조회하는 API입니다.<br>" +
            "사용자 챌린지 ID를 path variable로 전달받아 해당 사용자 챌린지의 인증 내역을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_401_01", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC_400_01", description = "❌ 챌린지 인증이 완료되지 않았습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC_404_01", description = "❌ 사용자 챌린지가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameter(name = "userChallengeId", description = "인증 내역을 조회할 사용자 챌린지의 ID", example = "1")
    ApiResponse<ChallengeResponseDTO.ProofDetailsDTO> getChallengeProofDetails(@PathVariable Long userChallengeId);


    @PatchMapping("{userChallengeId}")
    @Operation(summary = "사용자 챌린지 인증 내역 수정 API", description = "챌린지 인증 내역을 수정하는 API입니다. <br>" +
            "presigned url 생성 시 반환된 파일명을 certificationImage로 넘겨주세요. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_401_01", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC_400_01", description = "❌ 챌린지 인증이 완료되지 않았습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC_400_02", description = "❌ 챌린지 인증 내역에 수정사항이 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC_404_01", description = "❌ 사용자 챌린지가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameter(name = "userChallengeId", description = "인증 내역을 수정할 사용자 챌린지의 ID", example = "1")
    ApiResponse<ChallengeResponseDTO.ModifyProofDTO> updateChallengeProof(@PathVariable Long userChallengeId,
            @Valid @RequestBody(required = false) ChallengeRequestDTO.ProofRequestDTO updateRequest);

    @DeleteMapping("{userChallengeId}")
    @Operation(
            summary = "사용자 챌린지 삭제 API",
            description = "사용자의 특정 챌린지를 삭제하는 API입니다.<br>" +
                    "사용자 챌린지 ID를 path variable로 전달받아 해당 사용자 챌린지를 삭제합니다.<br>" +
                    "❗Request Header에 JWT Access Token 값을 넣어야 합니다.❗"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_401_01", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC_404_01", description = "❌ 사용자 챌린지가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC_409_01", description = "❌ 완료된 챌린지는 삭제가 불가합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameter(name = "userChallengeId", description = "삭제할 사용자 챌린지의 ID", example = "1")
    ApiResponse<Void> deleteChallenge(@PathVariable("userChallengeId") Long userChallengeId);
}
