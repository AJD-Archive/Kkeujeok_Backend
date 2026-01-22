package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "개인 대시보드 수정 요청 DTO")
public record PersonalDashboardUpdateReqDto(
        @Schema(description = "대시보드 제목", example = "수정된 개인 프로젝트")
        String title,

        @Schema(description = "대시보드 설명", example = "수정된 개인 프로젝트 설명입니다.")
        String description,

        @Schema(description = "공개 여부", example = "false")
        boolean isPublic,

        @Schema(description = "카테고리", example = "공부")
        String category
) {
}
