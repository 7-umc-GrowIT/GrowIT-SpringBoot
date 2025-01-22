package umc.GrowIT.Server.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.CustomException;
import umc.GrowIT.Server.util.security.SecurityUtils;
import umc.GrowIT.Server.image.presentation.dto.UploadImageResponse;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ImageService implements ImageUtils {

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.base-url}")
    private String baseUrl;

    private final AmazonS3 amazonS3;

    @Override
    public UploadImageResponse uploadImage(MultipartFile file) {
        String url = upload(file);
        return new UploadImageResponse(url);
    }

    @Override
    public String upload(MultipartFile file) {

        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new CustomException(ErrorStatus.S3_FILE_EMPTY);
        }

        if (file.getSize() / (1024 * 1024) > 10) {
            throw new CustomException(ErrorStatus.S3_FILE_OVER_SIZE);
        }


        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

        if (!(ext.equals("jpg")
                || ext.equals("heic")
                || ext.equals("jpeg")
                || ext.equals("png"))) {
            throw new CustomException(ErrorStatus.S3_BAD_FILE_EXTENSION);
        }

        String randomName = UUID.randomUUID().toString();
        String fileName = SecurityUtils.getCurrentUserId() + "|" + randomName + "." + ext;

        try {
            ObjectMetadata objMeta = new ObjectMetadata();
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            objMeta.setContentType(file.getContentType());
            objMeta.setContentLength(bytes.length);
            amazonS3.putObject(
                    new PutObjectRequest(bucket, fileName, file.getInputStream(), objMeta)
            );

        } catch (IOException e) {
            throw new CustomException(ErrorStatus.S3_FILE_UPLOAD_FAIL);
        }
        return baseUrl + "/" + fileName;
    }

    @Override
    public void delete(String profilePath) {
        String objectName = getBucketKey(profilePath);
        amazonS3.deleteObject(bucket, objectName);
    }

    @Override
    public String getBucketKey(String profilePath) {
        return profilePath.substring(profilePath.lastIndexOf('/') + 1);
    }
}
