package umc.GrowIT.Server.service.S3Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.S3Handler;

import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;
    @Value("${aws.s3.base-url}")
    private String baseUrl;

    public void delete(String profilePath) {
        String objectName = getBucketKey(profilePath);
        amazonS3.deleteObject(bucketName, objectName);
    }

    public String getBucketKey(String profilePath){
        if(profilePath == null) {
            throw new S3Handler(ErrorStatus.S3_FILE_DELETE_FAILED);
        }
        return profilePath.replace(baseUrl + "/", "");
    }

    // 🔹 파일 이름 검증
    private void validateFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new S3Handler(ErrorStatus.S3_FILE_NAME_REQUIRED);
        }

        // 허용된 확장자만 허용 (.jpg, .png, .gif)
        if (!fileName.matches(".*\\.(jpg|jpeg|png|gif)$")) {
            throw new S3Handler(ErrorStatus.S3_BAD_FILE_EXTENSION);
        }
    }

    // 🔹 Presigned URL 및 파일 URL 생성 메서드 (서비스 내부로 이동)
    public Map<String, String> generatePresignedUploadUrl(String fileName) {
        validateFileName(fileName); // 파일 검증

        String presignedUrl = generatePresignedUrlForUpload(fileName);
        String fileUrl = "https://" + bucketName + ".amazonaws.com/challenges/" + fileName;
        return Map.of(
                "presignedUrl", presignedUrl,
                "fileUrl", fileUrl
        );
    }

    // Presigned URL 생성 (업로드)
    public String generatePresignedUrlForUpload(String fileName) {
        // 파일 경로에 폴더 추가
        String objectKey = "challenges/" + fileName;

        // PreSigned URL 유효 시간 설정 (10분)
        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withMethod(HttpMethod.PUT) // 업로드용 (PUT 요청)
                .withExpiration(expiration);

        URL presignedUrl = amazonS3.generatePresignedUrl(request);
        return presignedUrl.toString();
    }

    // Presigned URL 생성 (다운로드)
    public String generatePresignedUrlForDownload(String fileName) {
        // 파일 경로에 폴더 추가
        String objectKey = "challenges/" + fileName;
        // PreSigned URL 유효 시간 설정 (10분)
        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withMethod(HttpMethod.GET) // 다운로드용 (GET 요청)
                .withExpiration(expiration);

        URL presignedUrl = amazonS3.generatePresignedUrl(request);
        return presignedUrl.toString();
    }}


