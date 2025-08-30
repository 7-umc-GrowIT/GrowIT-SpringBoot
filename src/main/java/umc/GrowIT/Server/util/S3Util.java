package umc.GrowIT.Server.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Util {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final S3Presigner s3Presigner;

    // presigned url 생성 (PUT)
    public String toCreatePresignedUrl(String key, String contentType) {
        if (key == null || key.isBlank()) {
            return null;
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(30))
                .putObjectRequest(putObjectRequest)
                .build();

        return s3Presigner.presignPutObject(putObjectPresignRequest).url().toString();
    }

    // presigned url 생성 (GET)
    public String toGetPresignedUrl(String key, Duration duration) {
        if (key == null || key.isBlank()) {
            return null;
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(duration)
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(getObjectPresignRequest).url().toString();
    }
}


