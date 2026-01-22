package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.domain.TeamDocument;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Schema(description = "팀 문서 요청 DTO")
public record TeamDocumentReqDto(
        @Schema(description = "문서 제목", example = "팀 회의록")
        String title,

        @Schema(description = "문서 내용", example = "오늘 회의에서는 다음 스프린트 계획을 논의했습니다.")
        String content,

        @Schema(description = "문서 카테고리", example = "회의록")
        String category,

        @Schema(description = "팀 대시보드 ID", example = "1")
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
