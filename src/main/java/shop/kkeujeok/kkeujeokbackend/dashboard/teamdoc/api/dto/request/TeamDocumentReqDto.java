package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request;

import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.domain.TeamDocument;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

public record TeamDocumentReqDto(
        String title,
        String content,
        String category,
        Long teamDashboardId

) {
    public TeamDocument toEntity(Member member, TeamDashboard teamDashboard) {
        return TeamDocument.builder()
                .author(member.getName())
                .picture(member.getPicture())
                .title(title)
                .content(content)
                .category(category)
                .teamDashboard(teamDashboard)
                .build();
    }
}
