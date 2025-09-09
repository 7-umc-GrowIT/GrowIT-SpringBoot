package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.CreditSource;
import umc.GrowIT.Server.domain.enums.DiaryType;
import umc.GrowIT.Server.service.diaryService.DiaryCommandService;
import umc.GrowIT.Server.service.diaryService.DiaryQueryService;
import umc.GrowIT.Server.web.controller.specification.DiarySpecification;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryRequestDTO;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;

@Tag(name = "Diary", description = "일기 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries")
public class DiaryController implements DiarySpecification {

    private final DiaryQueryService diaryQueryService;
    private final DiaryCommandService diaryCommandService;

    @GetMapping("/has-voice-diary")
    public ApiResponse<Boolean> hasVoiceDiary(){
        //accessToken에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.onSuccess(diaryQueryService.hasVoiceDiaries(userId));
    }

    @GetMapping("/dates")
    public ApiResponse<DiaryResponseDTO.DiaryDateListDTO> getDiaryDate(@RequestParam Integer year,
                                                                       @RequestParam Integer month){
        //accessToken에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.onSuccess(diaryQueryService.getDiaryDate(year,month,userId));
    }
    @GetMapping("/")
    public ApiResponse<DiaryResponseDTO.DiaryListDTO> getDiaryList(@RequestParam Integer year,
                                                                   @RequestParam Integer month){
        //accessToken에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.onSuccess(diaryQueryService.getDiaryList(year,month,userId));
    }
    @GetMapping("/{diaryId}")
    public ApiResponse<DiaryResponseDTO.DiaryDTO> getDiary(@PathVariable("diaryId") Long diaryId){
        //accessToken에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.onSuccess(diaryQueryService.getDiary(diaryId, userId));
    }

    @PatchMapping("/{diaryId}")
    public ApiResponse<DiaryResponseDTO.ModifyDiaryResultDTO> modifyDiary(@PathVariable("diaryId") Long diaryId, @Valid @RequestBody DiaryRequestDTO.ModifyDiaryDTO request){
        //accessToken에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.onSuccess(diaryCommandService.modifyDiary(request, diaryId, userId));
    }

    @DeleteMapping("/{diaryId}")
    public ApiResponse<Void> deleteDiary(@PathVariable("diaryId") Long diaryId){
        //accessToken에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        diaryCommandService.deleteDiary(diaryId, userId);
        return ApiResponse.onSuccess();
    }
    @PostMapping("/text")
    public ApiResponse<DiaryResponseDTO.CreateDiaryResultDTO> createDiaryByText(@Valid @RequestBody DiaryRequestDTO.CreateDiaryDTO request){
        //accessToken에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.onSuccess(diaryCommandService.createDiary(request, userId));
    }
    @PostMapping("/voice")
    public ApiResponse<DiaryResponseDTO.VoiceChatResultDTO> chatByVoice(@RequestBody DiaryRequestDTO.VoiceChatDTO request){

        //accessToken에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.onSuccess(diaryCommandService.chatByVoice(request, userId));
    }

    @PostMapping("/summary")
    public ApiResponse<DiaryResponseDTO.SummaryResultDTO> createDiaryByVoice(@RequestBody DiaryRequestDTO.SummaryDTO request) {

        //accessToken에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.onSuccess(diaryCommandService.createDiaryByVoice(request, userId));
    }

    @Override
    @PostMapping("/analyze/{diaryId}")
    public ApiResponse<DiaryResponseDTO.AnalyzedDiaryResponseDTO> analyzeDiary (@PathVariable("diaryId") Long diaryId) {
        DiaryResponseDTO.AnalyzedDiaryResponseDTO result = diaryCommandService.analyze(diaryId);
        return ApiResponse.onSuccess(result);
    }
}