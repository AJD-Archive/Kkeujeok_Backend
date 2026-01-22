package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Builder
@Schema(description = "개인 대시보드 정보 응답 DTO")
public record PersonalDashboardInfoResDto(
        @Schema(description = "대시보드 ID", example = "1")
        Long dashboardId,

        @Schema(description = "내 ID", example = "1")
        Long myId,

        @Schema(description = "생성자 ID", example = "1")
        Long creatorId,

        @Schema(description = "대시보드 제목", example = "개인 프로젝트")
        String title,

        @Schema(description = "대시보드 설명", example = "개인 프로젝트를 위한 대시보드입니다.")
        String description,

        @Schema(description = "공개 여부", example = "true")
        boolean isPublic,

        @Schema(description = "카테고리", example = "개발")
        String category,

        @Schema(description = "블록 진행률", example = "75.5")
        double blockProgress
) {
    public static PersonalDashboardInfoResDto of(Member member, PersonalDashboard dashboard) {
        return commonBuilder(member, dashboard)
                .build();
    }

    public static PersonalDashboardInfoResDto detailOf(Member member,
                                                       PersonalDashboard dashboard,
                                                       double blockProgress) {
        return commonBuilder(member, dashboard)
                .blockProgress(blockProgress)
                .build();
    }

    private static PersonalDashboardInfoResDtoBuilder commonBuilder(Member member,
                                                                    PersonalDashboard dashboard) {
        return PersonalDashboardInfoResDto.builder()
                .dashboardId(dashboard.getId())
                .myId(member.getId())
                .creatorId(dashboard.getMember().getId())
                .title(dashboard.getTitle())
                .description(dashboard.getDescription())
                .isPublic(dashboard.isPublic())
                .category(dashboard.getCategory());
    }

}
