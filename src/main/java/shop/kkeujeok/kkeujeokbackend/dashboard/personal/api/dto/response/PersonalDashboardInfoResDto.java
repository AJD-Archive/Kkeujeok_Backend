package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;

@Builder
public record PersonalDashboardInfoResDto(
        String title,
        String description,
        boolean isPublic,
        String category
) {
    public static PersonalDashboardInfoResDto from(PersonalDashboard dashboard) {
        return PersonalDashboardInfoResDto.builder()
                .title(dashboard.getTitle())
                .description(dashboard.getDescription())
                .isPublic(dashboard.isPublic())
                .category(dashboard.getCategory())
                .build();
    }
}
