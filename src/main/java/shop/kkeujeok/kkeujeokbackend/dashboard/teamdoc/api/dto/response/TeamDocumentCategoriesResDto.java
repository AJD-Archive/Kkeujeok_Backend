package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record TeamDocumentCategoriesResDto(
        List<String> categories
) {
    public static TeamDocumentCategoriesResDto of(List<String> categories) {
        return TeamDocumentCategoriesResDto.builder()
                .categories(categories)
                .build();
    }
}
