package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.util.List;

@Builder
@Schema(description = "팀 문서 카테고리 목록 응답 DTO")
public record TeamDocumentCategoriesResDto(
        @Schema(description = "카테고리 목록", example = "[\"회의록\", \"업무 일지\"]")
        @NotNull(message = "카테고리 목록은 필수입니다.")
        List<String> categories
) {
    public static TeamDocumentCategoriesResDto of(List<String> categories) {
        return TeamDocumentCategoriesResDto.builder()
                .categories(categories)
                .build();
    }
}
