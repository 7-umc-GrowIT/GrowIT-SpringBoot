package umc.GrowIT.Server.web.controller.specification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import umc.GrowIT.Server.apiPayload.ApiResponse;

import java.util.Map;

public interface S3Specification {
    @PutMapping("upload-url")
    @Operation(summary = "PresignedUrl 업로드 API", description = "서버에서 presignedUrl을 발급받아 클라이언트에 넘겨주고, 클라이언트에서 발급받은 presignedUrl과 함께 이미지를 업로드합니다. <br> " +
            "폴더명과 파일명(확장자 포함)을 입력해주세요. 챌린지 인증 이미지를 업로드하려면 폴더명에 challenges를 입력해주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "S3_4001", description = "❌ 파일 확장자가 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "S3_4002", description = "❌ 파일 이름은 필수입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "S3_4003", description = "❌ 폴더 이름은 필수입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "S3_4004", description = "❌ 폴더명은 영어로 입력해야 합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))


    })
    ApiResponse<Map<String, String>> getPresignedUploadUrl(@RequestParam String folder, @RequestParam String fileName);

    @GetMapping("download-url")
    @Operation(summary = "PresignedUrl 다운로드 API", description = "클라이언트에서 업로드한 이미지의 폴더명과 파일명을 입력해주세요. <br> " +
            "파일명은 확장자까지 입력해야 합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "S3_4001", description = "❌ 파일 확장자가 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "S3_4002", description = "❌ 파일 이름은 필수입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "S3_4003", description = "❌ 폴더 이름은 필수입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "S3_4004", description = "❌ 폴더명은 영어로 입력해야 합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<String> getPresignedDownloadUrl(@RequestParam String folder, @RequestParam String fileName);
}