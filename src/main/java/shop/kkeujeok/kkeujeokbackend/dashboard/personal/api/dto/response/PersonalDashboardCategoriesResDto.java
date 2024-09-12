package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response;

import java.util.List;
import java.util.Set;
import lombok.Builder;

@Builder
public record PersonalDashboardCategoriesResDto(
        Set<String> categories
) {
    public static PersonalDashboardCategoriesResDto from(Set<String> categories) {
        return PersonalDashboardCategoriesResDto.builder()
                .categories(categories)
                .build();
    }
}
