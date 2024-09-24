package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

import java.util.List;

@Builder
public record PersonalDashboardPageListResDto(
        List<PersonalDashboardInfoResDto> personalDashboardInfoResDto,
        PageInfoResDto pageInfoResDto
) {
    public static PersonalDashboardPageListResDto of(List<PersonalDashboardInfoResDto> personalDashboards,
                                                     PageInfoResDto pageInfoResDto) {
        return PersonalDashboardPageListResDto.builder()
                .personalDashboardInfoResDto(personalDashboards)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }

    public static PersonalDashboardPageListResDto from(List<PersonalDashboardInfoResDto> personalDashboards) {
        return PersonalDashboardPageListResDto.builder()
                .personalDashboardInfoResDto(personalDashboards)
                .build();
    }
}
