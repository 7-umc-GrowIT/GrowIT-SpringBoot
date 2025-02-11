package umc.GrowIT.Server.service.S3Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

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


