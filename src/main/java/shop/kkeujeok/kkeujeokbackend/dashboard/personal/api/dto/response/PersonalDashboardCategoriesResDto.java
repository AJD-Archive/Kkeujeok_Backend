package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Set;
import lombok.Builder;

@Builder
@Schema(description = "개인 대시보드 카테고리 목록 응답 DTO")
public record PersonalDashboardCategoriesResDto(
        @Schema(description = "카테고리 목록", example = "[\"개발\", \"공부\", \"운동\"]")
        Set<String> categories
) {
    public static PersonalDashboardCategoriesResDto from(Set<String> categories) {
        return PersonalDashboardCategoriesResDto.builder()
                .categories(categories)
                .build();
    }
}
