package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

public record PersonalDashboardSaveReqDto(
        @NotBlank(message = "필수 입력값 입니다.")
        String title,

        @NotBlank(message = "필수 입력값 입니다.")
        @Size(max = 300)
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
