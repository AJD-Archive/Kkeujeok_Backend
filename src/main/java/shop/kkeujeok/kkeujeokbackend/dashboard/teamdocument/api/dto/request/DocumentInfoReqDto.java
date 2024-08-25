package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.request;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Builder
public record DocumentInfoReqDto(
        Long teamDashboardId,
        String title
) {
    public Document toEntity(TeamDashboard teamDashboard) {
        return Document.builder()
                .title(title)
                .teamDashboard(teamDashboard)
                .build();
    }
}
