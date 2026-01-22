package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Schema(description = "개인 대시보드 생성 요청 DTO")
public record PersonalDashboardSaveReqDto(
        @Schema(description = "대시보드 제목", example = "개인 프로젝트")
        @NotBlank(message = "필수 입력값 입니다.")
        String title,

        @Schema(description = "대시보드 설명", example = "개인 프로젝트를 위한 대시보드입니다.")
        @NotBlank(message = "필수 입력값 입니다.")
        @Size(max = 300)
        String description,

        @Schema(description = "공개 여부", example = "true")
        boolean isPublic,

        @Schema(description = "카테고리", example = "개발")
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
