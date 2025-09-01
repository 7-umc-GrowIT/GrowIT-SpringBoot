package umc.GrowIT.Server.web.dto.UserDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.Map;

public class UserResponseDTO {

    // 마이페이지 조회 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageDTO {
        @Schema(description = "유저 ID", example="1")
        private Long userId;
        @Schema(description = "그로의 닉네임", example = "그로우")
        private String name;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "크레딧 이력 조회 response")
    public static class CreditHistoryResponseDTO {

        @Schema(description = "조회 년월", example = "2025년 08월")
        private String yearMonth;

        @Schema(description = "날짜별 크레딧 내역")
        private Map<String, List<CreditRecordDTO>> dailyHistories;

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        private Boolean hasNext;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "크레딧 내역")
    public static class CreditRecordDTO {

        @Schema(description = "시간", example = "15:33:04")
        private String time;

        @Schema(description = "설명", example = "[08-30] 일기 작성")
        private String description;

        @Schema(description = "크레딧 양", example = "3000")
        private Integer amount;

        @Schema(description = "타입", example = "적립")
        private String type;
    }
}
