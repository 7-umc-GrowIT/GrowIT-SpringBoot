package umc.GrowIT.Server.web.controller.specification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryRequestDTO;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;

public interface DiarySpecification {
    @GetMapping("/dates")
    @Operation(summary = "일기 작성 날짜 조회 API",description = "특정 사용자의 일기 메인 화면에서 사용할 월별 일기 기록한 날짜를 보여주기 위한 API입니다. Query String으로 year와 month를 주세요." +
            "ex)2024년 12월을 넘겨주면 해당 월에 기록한 일기들의 날짜 정보와 해당 일기의 id를 보내줍니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DATE4001", description = "유효하지 않은 날짜입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<DiaryResponseDTO.DiaryDateListDTO> getDiaryDate(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                                                @RequestParam Integer year, @RequestParam Integer month);

    @GetMapping("/")
    @Operation(summary = "일기 모아보기 API",description = "특정 사용자가 작성한 일기를 모아보는 API입니다. query string으로 year와 month를 넘겨주면 해당 월에 작성한 일기들의 리스트, 작성한 일기 수를 보내줍니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DATE4001", description = "유효하지 않은 날짜입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<DiaryResponseDTO.DiaryListDTO> getDiaryList(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                                            @RequestParam Integer year,
                                                            @RequestParam Integer month);

    @GetMapping("/{diaryId}")
    @Operation(summary = "특정 일기 조회 API",description = "특정 사용자가 작성한 특정 일기를 조회하는 API입니다. path variable로 일기의 id를 넘겨주면 해당 일기의 세부적인 내용을 보내줍니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY4001", description = "존재하지 않는 일기입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<DiaryResponseDTO.DiaryDTO> getDiary(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                                    @PathVariable("diaryId") Long diaryId);

    @PatchMapping("/{diaryId}")
    @Operation(summary = "일기 수정하기 API",description = "특정 사용자가 작성한 일기를 수정하는 API입니다. path variable로 일기의 id, RequestBody로 수정한 내용과 수정 시간을 보내주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY4001", description = "존재하지 않는 일기입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY4004",description = "기존 일기와 동일한 내용입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<DiaryResponseDTO.ModifyDiaryResultDTO> modifyDiary(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                                                          @PathVariable("diaryId") Long diaryId, @RequestBody DiaryRequestDTO.ModifyDiaryDTO request);

    @DeleteMapping("/{diaryId}")
    @Operation(summary = "일기 삭제하기 API",description = "특정 사용자가 작성한 일기를 삭제하는 API입니다. path variable로 일기의 id를 보내주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY4001", description = "존재하지 않는 일기입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<DiaryResponseDTO.DeleteDiaryResultDTO> deleteDiary(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                                                   @PathVariable("diaryId") Long diaryId);

    @PostMapping("/text")
    @Operation(summary = "직접 일기 작성하기 API",description = "특정 사용자가 일기를 텍스트로 직접 작성하는 API입니다. 작성내용과 작성일자를 보내주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY4002",description = "100자 이내로 작성된 일기입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DATE4002",description = "날짜는 오늘 이후로 설정할 수 없습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<DiaryResponseDTO.CreateDiaryResultDTO> createDiaryByText(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                                                         @RequestBody DiaryRequestDTO.CreateDiaryDTO request);

    @PostMapping("/voice")
    @Operation(summary = "음성으로 일기 작성하기 API",description = "특정 사용자가 일기를 음성으로 말하며 작성하는 API입니다. 음성내용과 작성일자를 보내주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY4002",description = "100자 이내로 작성된 일기입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<DiaryResponseDTO.CreateDiaryResultDTO> createDiaryByVoice(@RequestBody DiaryRequestDTO.CreateDiaryDTO request);
}
