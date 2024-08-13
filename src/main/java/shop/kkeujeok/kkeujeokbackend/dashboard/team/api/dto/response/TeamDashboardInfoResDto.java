package shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Builder
public record TeamDashboardInfoResDto(
        Long dashboardId,
        Long myId,
        Long creatorId,
        String title,
        String description,
        double blockProgress
) {
    public static TeamDashboardInfoResDto of(Member member, TeamDashboard dashboard) {
        return commonBuilder(member, dashboard)
                .build();
    }

    public static TeamDashboardInfoResDto detailOf(Member member, TeamDashboard dashboard, double blockProgress) {
        return commonBuilder(member, dashboard)
                .blockProgress(blockProgress)
                .build();
    }

    public static TeamDashboardInfoResDtoBuilder commonBuilder(Member member, TeamDashboard dashboard) {
        return TeamDashboardInfoResDto.builder()
                .dashboardId(dashboard.getId())
                .myId(member.getId())
                .creatorId(dashboard.getMember().getId())
                .title(dashboard.getTitle())
                .description(dashboard.getDescription());
    }
}
