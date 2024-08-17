package shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

public record TeamDashboardSaveReqDto(
        @NotBlank(message = "필수 입력값 입니다.")
        String title,

        @NotBlank(message = "필수 입력값 입니다.")
        @Size(max = 300)
        String description
) {
    public TeamDashboard toEntity(Member member) {
        return TeamDashboard.builder()
                .title(title)
                .description(description)
                .member(member)
                .build();
    }
}
