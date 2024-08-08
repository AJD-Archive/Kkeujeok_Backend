package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request;

import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

public record PersonalDashboardSaveReqDto(
        String title,
        String description,
        boolean isPublic,
        String category
) {
    public PersonalDashboard toEntity(Member member) {
        return PersonalDashboard.builder()
                .title(title)
                .description(description)
                .member(member)
                .isPublic(isPublic)
                .category(category)
                .build();
    }
}
