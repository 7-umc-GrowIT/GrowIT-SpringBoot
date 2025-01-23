package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.service.diaryService.DiaryCommandService;
import umc.GrowIT.Server.service.diaryService.DiaryQueryService;
import umc.GrowIT.Server.web.controller.specification.DiarySpecification;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryRequestDTO;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;

@Tag(name = "Diary", description = "일기 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries")
public class DiaryController implements DiarySpecification {
    private final DiaryQueryService diaryQueryService;
    private final DiaryCommandService diaryCommandService;
    @GetMapping("/dates")
    public ApiResponse<DiaryResponseDTO.DiaryDateListDTO> getDiaryDate(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                                                       @RequestParam Integer year,
                                                                       @RequestParam Integer month){
        //accessToken에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.onSuccess(diaryQueryService.getDiaryDate(year,month,userId));
    }
    @GetMapping("/")
    public ApiResponse<DiaryResponseDTO.DiaryListDTO> getDiaryList(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                                                   @RequestParam Integer year,
                                                                   @RequestParam Integer month){
        //accessToken에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.onSuccess(diaryQueryService.getDiaryList(year,month,userId));
    }
    @GetMapping("/{diaryId}")
    public ApiResponse<DiaryResponseDTO.DiaryDTO> getDiary(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                                           @PathVariable("diaryId") Long diaryId){
        //accessToken에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.onSuccess(diaryQueryService.getDiary(diaryId, userId));
    }

    @PatchMapping("/{diaryId}")
    public ApiResponse<DiaryResponseDTO.ModifyResultDTO> modifyDiary(@RequestHeader(value = "Authorization", required = false) String authorizationHeader,
                                                                     @PathVariable("diaryId") Long diaryId, @RequestBody DiaryRequestDTO.DiaryDTO request){
        //accessToken에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.onSuccess(diaryCommandService.modifyDiary(request, diaryId, userId));
    }

    @DeleteMapping("/{diaryId}")
    public void deleteDiary(@PathVariable("diaryId") Long diaryId){

    }
    @PostMapping("/text")
    public ApiResponse<DiaryResponseDTO.CreateResultDTO> createDiaryByText(@RequestBody DiaryRequestDTO.DiaryDTO request){

        return null;
    }
    @PostMapping("/voice")
    public ApiResponse<DiaryResponseDTO.CreateResultDTO> createDiaryByVoice(@RequestBody DiaryRequestDTO.DiaryDTO request){
        //Todo: 음성인식 결과를 다듬어주는 로직 필요
        return null;
    }




}