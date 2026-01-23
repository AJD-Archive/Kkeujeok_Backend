package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "개인 대시보드 수정 요청 DTO")
public record PersonalDashboardUpdateReqDto(
        @Schema(description = "대시보드 제목", example = "수정된 개인 프로젝트")
        @NotNull(message = "대시보드 제목은 필수입니다.")
        String title,

        @Schema(description = "대시보드 설명", example = "수정된 개인 프로젝트 설명입니다.")
        @NotNull(message = "대시보드 설명은 필수입니다.")
        String description,

        @Schema(description = "공개 여부", example = "false")
        boolean isPublic, // boolean은 primitive type이므로 @NotNull 사용 불가

        @Schema(description = "카테고리", example = "공부")
        @NotNull(message = "카테고리는 필수입니다.")
        String category
) {
}
