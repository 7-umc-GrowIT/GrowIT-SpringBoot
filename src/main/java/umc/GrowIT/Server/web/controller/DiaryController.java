package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/voice/exists")
    public ApiResponse<Boolean> hasVoiceDiary(@AuthenticationPrincipal Long userId){
        return ApiResponse.onSuccess(diaryQueryService.hasVoiceDiaries(userId));
    }

    @GetMapping("/dates")
    public ApiResponse<DiaryResponseDTO.DiaryDateListDTO> getDiaryDate(@AuthenticationPrincipal Long userId,
                                                                       @RequestParam Integer year,
                                                                       @RequestParam Integer month){
        return ApiResponse.onSuccess(diaryQueryService.getDiaryDate(year,month,userId));
    }
    @GetMapping("")
    public ApiResponse<DiaryResponseDTO.DiaryListDTO> getDiaryList(@AuthenticationPrincipal Long userId,
                                                                   @RequestParam Integer year,
                                                                   @RequestParam Integer month){
        return ApiResponse.onSuccess(diaryQueryService.getDiaryList(year,month,userId));
    }
    @GetMapping("/{diaryId}")
    public ApiResponse<DiaryResponseDTO.DiaryDTO> getDiary(@AuthenticationPrincipal Long userId, @PathVariable("diaryId") Long diaryId){
        return ApiResponse.onSuccess(diaryQueryService.getDiary(diaryId, userId));
    }

    @PatchMapping("/{diaryId}")
    public ApiResponse<DiaryResponseDTO.ModifyDiaryResultDTO> modifyDiary(@AuthenticationPrincipal Long userId, @PathVariable("diaryId") Long diaryId, @Valid @RequestBody DiaryRequestDTO.ModifyDiaryDTO request){
        return ApiResponse.onSuccess(diaryCommandService.modifyDiary(request, diaryId, userId));
    }

    @DeleteMapping("/{diaryId}")
    public ApiResponse<Void> deleteDiary(@AuthenticationPrincipal Long userId, @PathVariable("diaryId") Long diaryId){
        diaryCommandService.deleteDiary(diaryId, userId);
        return ApiResponse.onSuccess();
    }
    @PostMapping("/text")
    public ApiResponse<DiaryResponseDTO.SaveDiaryResultDTO> saveDiaryByText(@AuthenticationPrincipal Long userId, @Valid @RequestBody DiaryRequestDTO.SaveTextDiaryDTO request){
        return ApiResponse.onSuccess(diaryCommandService.saveDiaryByText(request, userId));
    }
    @PostMapping("/voice/chat")
    public ApiResponse<DiaryResponseDTO.VoiceChatResultDTO> chatByVoice(@AuthenticationPrincipal Long userId, @RequestBody DiaryRequestDTO.VoiceChatDTO request){
        if (!request.getHasAdditionalChat()) {
            return ApiResponse.onSuccess(diaryCommandService.chatByVoice(request, userId));
        } else {
            return ApiResponse.onSuccess(diaryCommandService.additionalChatByVoice(request, userId));
        }
    }

    @PostMapping("/voice")
    public ApiResponse<DiaryResponseDTO.SaveDiaryResultDTO> saveDiaryByVoice(@AuthenticationPrincipal Long userId, @RequestBody DiaryRequestDTO.SaveVoiceDiaryDTO request) {
        return ApiResponse.onSuccess(diaryCommandService.saveDiaryByVoice(request, userId));
    }

    @Override
    @PostMapping("/{diaryId}/analyze")
    public ApiResponse<DiaryResponseDTO.AnalyzedDiaryResponseDTO> analyzeDiary (@PathVariable("diaryId") Long diaryId) {
        DiaryResponseDTO.AnalyzedDiaryResponseDTO result = diaryCommandService.analyze(diaryId);
        return ApiResponse.onSuccess(result);
    }
}
