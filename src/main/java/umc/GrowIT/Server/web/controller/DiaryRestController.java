package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.web.dto.DiaryRequestDTO;
import umc.GrowIT.Server.web.dto.DiaryResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries")
public class DiaryRestController {
    @GetMapping("/dates")
    @Operation(summary = "일기 작성 날짜 조회 API",description = "특정 사용자의 일기 메인 화면에서 사용할 월별 일기 기록한 날짜를 보여주기 위한 API입니다. Query String으로 year와 month를 주세요." +
            "ex)2024년 12월을 넘겨주면 해당 월에 기록한 일기들의 날짜 정보와 해당 일기의 id를 보내줍니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DATE4001", description = "유효하지 않은 날짜입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<DiaryResponseDTO.DiaryDateListDTO> getDiaryDate(@RequestParam Integer year, @RequestParam Integer month){

        return null;
    }
    @GetMapping("/")
    @Operation(summary = "일기 모아보기 API",description = "특정 사용자가 작성한 일기를 모아보는 API입니다. query string으로 year와 month를 넘겨주면 해당 월에 작성한 일기들의 리스트, 작성한 일기 수를 보내줍니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DATE4001", description = "유효하지 않은 날짜입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<DiaryResponseDTO.DiaryListDTO> getDiaryList(@RequestParam Integer year, @RequestParam Integer month){

        return null;
    }
    @GetMapping("/{diaryId}")
    @Operation(summary = "특정 일기 조회 API",description = "특정 사용자가 작성한 특정 일기를 조회하는 API입니다. path variable로 일기의 id를 넘겨주면 해당 일기의 세부적인 내용을 보내줍니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY4001", description = "존재하지 않는 일기입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<DiaryResponseDTO.DiaryDTO> getDiary(@PathVariable("diaryId") Long diaryId){

        return null;
    }

    @PutMapping("/{diaryId}")
    @Operation(summary = "일기 수정하기 API",description = "특정 사용자가 작성한 일기를 수정하는 API입니다. path variable로 일기의 id, RequestBody로 수정한 내용과 수정 시간을 보내주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY4001", description = "존재하지 않는 일기입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY4002",description = "100자 이내로 작성된 일기입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<DiaryResponseDTO.ModifyResultDTO> modifyDiary(@PathVariable("diaryId") Long diaryId, @RequestBody DiaryRequestDTO.DiaryDTO request){

        return null;
    }

    @DeleteMapping("/{diaryId}")
    @Operation(summary = "일기 삭제하기 API",description = "특정 사용자가 작성한 일기를 삭제하는 API입니다. path variable로 일기의 id를 보내주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY4001", description = "존재하지 않는 일기입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public void deleteDiary(@PathVariable("diaryId") Long diaryId){

    }
    @PostMapping("/text")
    @Operation(summary = "직접 일기 작성하기 API",description = "특정 사용자가 일기를 텍스트로 직접 작성하는 API입니다. 작성내용과 작성일자를 보내주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY4002",description = "100자 이내로 작성된 일기입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<DiaryResponseDTO.CreateResultDTO> createDiaryByText(@RequestBody DiaryRequestDTO.DiaryDTO request){

        return null;
    }
    @PostMapping("/voice")
    @Operation(summary = "음성으로 일기 작성하기 API",description = "특정 사용자가 일기를 음성으로 말하며 작성하는 API입니다. 음성내용과 작성일자를 보내주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "DIARY4002",description = "100자 이내로 작성된 일기입니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<DiaryResponseDTO.CreateResultDTO> createDiaryByVoice(@RequestBody DiaryRequestDTO.DiaryDTO request){
        //Todo: 음성인식 결과를 다듬어주는 로직 필요
        return null;
    }




}