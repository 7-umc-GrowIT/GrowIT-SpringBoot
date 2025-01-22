package umc.GrowIT.Server.image.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import umc.GrowIT.Server.image.service.ImageService;
import umc.GrowIT.Server.image.presentation.dto.UploadImageResponse;

@Tag(name = "업로드", description = "업로드 관련 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
@RestController
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "사진 업로드")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public UploadImageResponse uploadImage(
            @Parameter(name = "file",
                    description = "multipart/form-data 형식의 이미지를 input으로 받습니다.",
                    required = true)
            @RequestPart MultipartFile file) {

        log.info("file = {}",file);
        return imageService.uploadImage(file);
    }
}
