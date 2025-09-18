package umc.GrowIT.Server.web.controller.specification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryRequestDTO;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;

public interface DiarySpecification {
    @GetMapping("/voice/exists")
    @Operation(summary = "음성 일기 작성 여부 조회 API",description = "사용자가 한번이라도 음성 일기를 작성했는지의 여부를 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
    })
    ApiResponse<Boolean> hasVoiceDiary(@AuthenticationPrincipal Long userId);

    @GetMapping("/dates")
    @Operation(summary = "일기 작성 날짜 조회 API",description = "특정 사용자의 일기 메인 화면에서 사용할 월별 일기 기록한 날짜를 보여주기 위한 API입니다. Query String으로 year와 month를 주세요." +
            "ex)2025년 9월을 넘겨주면 해당 월에 기록한 일기들의 날짜 정보와 해당 일기의 id를 보내줍니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DATE_400_01", description = "❌ 유효하지 않은 날짜입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<DiaryResponseDTO.DiaryDateListDTO> getDiaryDate(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "일기 작성 연도",
                    example = "2025")@RequestParam Integer year,
            @Parameter(description = "일기 작성 월",
                    example = "9")@RequestParam Integer month);

    @GetMapping("")
    @Operation(summary = "일기 모아보기 API",description = "특정 사용자가 작성한 일기를 모아보는 API입니다. query string으로 year와 month를 넘겨주면 해당 월에 작성한 일기들의 리스트, 작성한 일기 수를 보내줍니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DATE_400_01", description = "❌ 유효하지 않은 날짜입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<DiaryResponseDTO.DiaryListDTO> getDiaryList(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "일기 작성 연도",
                    example = "2025")@RequestParam Integer year,
            @Parameter(description = "일기 작성 월",
                    example = "9")@RequestParam Integer month);

    @GetMapping("/{diaryId}")
    @Operation(summary = "특정 일기 조회 API",description = "특정 사용자가 작성한 특정 일기를 조회하는 API입니다. path variable로 일기의 id를 넘겨주면 해당 일기의 세부적인 내용을 보내줍니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY_404_01", description = "❌ 존재하지 않는 일기입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameter(name = "diaryId", description = "조회할 일기의 ID", example = "1")
    ApiResponse<DiaryResponseDTO.DiaryDTO> getDiary(@AuthenticationPrincipal Long userId, @PathVariable("diaryId") Long diaryId);

    @PatchMapping("/{diaryId}")
    @Operation(summary = "일기 수정하기 API",description = "특정 사용자가 작성한 일기를 수정하는 API입니다. path variable로 일기의 id, RequestBody로 수정한 내용과 수정 시간을 보내주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200",description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400",description = "❌ 일기는 100자 이상으로 작성해야 합니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY_404_01", description = "❌ 존재하지 않는 일기입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY_400_02",description = "❌ 기존 일기와 동일한 내용입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameter(name = "diaryId", description = "수정할 일기의 ID", example = "1")
    ApiResponse<DiaryResponseDTO.ModifyDiaryResultDTO> modifyDiary(@AuthenticationPrincipal Long userId, @PathVariable("diaryId") Long diaryId, @Valid @RequestBody DiaryRequestDTO.ModifyDiaryDTO request);

    @DeleteMapping("/{diaryId}")
    @Operation(summary = "일기 삭제하기 API",description = "특정 사용자가 작성한 일기를 삭제하는 API입니다. path variable로 일기의 id를 보내주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY_404_01", description = "❌ 존재하지 않는 일기입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameter(name = "diaryId", description = "삭제할 일기의 ID", example = "1")
    ApiResponse<Void> deleteDiary(@AuthenticationPrincipal Long userId, @PathVariable("diaryId") Long diaryId);

    @PostMapping("/text")
    @Operation(summary = "직접 작성한 일기 임시저장 API",description = "직접 작성한 일기를 임시저장하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400",description = "❌ 일기는 100자 이상으로 작성해야 합니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DATE_400_02",description = "❌ 날짜는 오늘 이후로 설정할 수 없습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<DiaryResponseDTO.SaveDiaryResultDTO> saveDiaryByText(@AuthenticationPrincipal Long userId, @Valid @RequestBody DiaryRequestDTO.SaveTextDiaryDTO request);

    @PostMapping("/voice/chat")
    @Operation(summary = "AI와 음성 대화 API",description = "AI와 자신의 하루를 음성으로 대화 나누는 API입니다. 음성내용(STT)을 보내주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
    })
    ApiResponse<DiaryResponseDTO.VoiceChatResultDTO> chatByVoice(@AuthenticationPrincipal Long userId, @RequestBody DiaryRequestDTO.VoiceChatDTO request);

    @PostMapping("/voice")
    @Operation(summary = "음성으로 작성한 일기 임시저장 API",description = "AI와 대화한 내용을 바탕으로 생성한 일기를 임시저장하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
    })
    ApiResponse<DiaryResponseDTO.SaveDiaryResultDTO> saveDiaryByVoice(@AuthenticationPrincipal Long userId, @RequestBody DiaryRequestDTO.SaveVoiceDiaryDTO request);

    @PostMapping("/{diaryId}/analyze")
    @Operation(
            summary = "일기 분석 API",
            description = "일기 내용을 분석하여, 적절한 감정과 데일리/랜덤 챌린지를 반환하는 API입니다.<br>" +
                    "일기 ID를 path variable로 전달받아 해당 일기를 분석합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY_404_01", description = "❌ 존재하지 않는 일기입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY_409_02", description = "❌ 이미 분석된 일기입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "GPT_500_01", description = "❌ GPT 응답이 비어있습니다. 다시 시도해 주세요.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "GPT_500_02", description = "❌ GPT 응답에서 감정의 개수는 3개여야 합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "GPT_500_03", description = "❌ GPT 응답에서 중복된 감정이 존재합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "FLASK_500_01", description = "❌ Flask API 호출에 실패하였습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "KEYWORD_404_01", description = "❌ 감정키워드가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHALLENGE_404_02", description = "❌ 연관된 챌린지가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameter(name = "diaryId", description = "분석할 일기의 ID", example = "1")
    ApiResponse<DiaryResponseDTO.AnalyzedDiaryResponseDTO> analyzeDiary(@PathVariable("diaryId") Long diaryId);
}
