package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Builder
public record PersonalDashboardInfoResDto(
        Long dashboardId,
        Long myId,
        Long creatorId,
        String title,
        String description,
        boolean isPublic,
        String category
) {
    public static PersonalDashboardInfoResDto of(Member member, PersonalDashboard dashboard) {
        return PersonalDashboardInfoResDto.builder()
                .dashboardId(dashboard.getId())
                .myId(member.getId())
                .creatorId(dashboard.getMember().getId())
                .title(dashboard.getTitle())
                .description(dashboard.getDescription())
                .isPublic(dashboard.isPublic())
                .category(dashboard.getCategory())
                .build();
    }
}
