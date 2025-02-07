package umc.GrowIT.Server.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.service.S3Service.S3Service;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    // ✅ S3 파일 업로드용 PreSigned URL 발급
    @GetMapping("/upload-url")
    public ResponseEntity<Map<String, String>> getPresignedUploadUrl(@RequestParam String fileName) {
        // ✅ 파일 이름 검증
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("파일 이름은 필수입니다.");
        }

        // ✅ 허용된 확장자만 허용 (.jpg, .png, .gif)
        if (!fileName.matches(".*\\.(jpg|jpeg|png|gif)$")) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다. (jpg, jpeg, png, gif만 허용)");
        }

        // ✅ 파일 이름에 UUID 추가하여 중복 방지
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;

        // PreSigned URL 생성
        String presignedUrl = s3Service.generatePresignedUrlForUpload(uniqueFileName);
        String fileUrl = "https://s3-bucket-url.amazonaws.com/challenges/" + uniqueFileName;

        Map<String, String> response = Map.of(
                "presignedUrl", presignedUrl,
                "fileUrl", fileUrl
        );

        return ResponseEntity.ok(response);
    }

    // ✅ S3 파일 다운로드용 PreSigned URL 발급
    @GetMapping("/download-url")
    public ResponseEntity<String> getPresignedDownloadUrl(@RequestParam String fileName) {
        String presignedUrl = s3Service.generatePresignedUrlForDownload(fileName);
        return ResponseEntity.ok(presignedUrl);
    }
}

