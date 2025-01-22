package umc.GrowIT.Server.image.service;

import org.springframework.web.multipart.MultipartFile;
import umc.GrowIT.Server.image.presentation.dto.UploadImageResponse;

public interface ImageUtils {
    void delete(String objectName);

    String upload(MultipartFile file);

    UploadImageResponse uploadImage(MultipartFile file);

    String getBucketKey(String profilePath);
}
