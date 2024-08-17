package shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response;

import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

@Builder
public record TeamDashboardListResDto(
        List<TeamDashboardInfoResDto> teamDashboardInfoResDto,
        PageInfoResDto pageInfoResDto
) {
    public static TeamDashboardListResDto of(List<TeamDashboardInfoResDto> teamDashboards,
                                             PageInfoResDto pageInfoResDto) {
        return TeamDashboardListResDto.builder()
                .teamDashboardInfoResDto(teamDashboards)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
