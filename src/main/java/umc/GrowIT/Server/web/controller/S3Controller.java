package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.service.s3Service.S3Service;
import umc.GrowIT.Server.web.controller.specification.S3Specification;

import java.util.Map;

@Tag(name="S3", description = "S3 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller implements S3Specification {

    private final S3Service s3Service;

    // S3 파일 업로드용 PreSigned URL 발급
    @PutMapping("upload-url")
    public ApiResponse<Map<String, String>> getPresignedUploadUrl(@RequestParam String folder, @RequestParam String fileName) {

        Map<String, String> response = s3Service.generatePresignedUploadUrl(folder, fileName);
        return ApiResponse.onSuccess(response);
    }

    // S3 파일 다운로드용 PreSigned URL 발급
    @GetMapping("download-url")
    public ApiResponse<String> getPresignedDownloadUrl(@RequestParam String folder, @RequestParam String fileName) {
        String presignedUrl = s3Service.generatePresignedUrlForDownload(folder, fileName);
        return ApiResponse.onSuccess(presignedUrl);
    }
}

