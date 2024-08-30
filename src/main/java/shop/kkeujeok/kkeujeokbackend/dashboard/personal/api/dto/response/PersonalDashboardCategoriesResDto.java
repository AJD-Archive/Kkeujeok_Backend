package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record PersonalDashboardCategoriesResDto(
        List<String> categories
) {
    public static PersonalDashboardCategoriesResDto from(List<String> categories) {
        return PersonalDashboardCategoriesResDto.builder()
                .categories(categories)
                .build();
    }
}
