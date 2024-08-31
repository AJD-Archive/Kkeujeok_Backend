package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record PersonalDashboardListResDto(
        List<PersonalDashboardInfoResDto> personalDashboardListResDto
) {
    public static PersonalDashboardListResDto of(List<PersonalDashboardInfoResDto> personalDashboards) {
        return PersonalDashboardListResDto.builder()
                .personalDashboardListResDto(personalDashboards)
                .build();
    }
}
