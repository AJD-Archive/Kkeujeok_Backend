package shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;

@Builder
@Schema(description = "팀 대시보드 정보 응답 DTO")
public record TeamDashboardInfoResDto(
        @Schema(description = "대시보드 ID", example = "1")
        @NotNull(message = "대시보드 ID는 필수입니다.")
        Long dashboardId,

        @Schema(description = "내 ID", example = "1")
        @NotNull(message = "내 ID는 필수입니다.")
        Long myId,

        @Schema(description = "생성자 ID", example = "2")
        @NotNull(message = "생성자 ID는 필수입니다.")
        Long creatorId,

        @Schema(description = "대시보드 제목", example = "팀 프로젝트")
        @NotNull(message = "대시보드 제목은 필수입니다.")
        String title,

        @Schema(description = "대시보드 설명", example = "팀 프로젝트를 위한 대시보드입니다.")
        @NotNull(message = "대시보드 설명은 필수입니다.")
        String description,

        @Schema(description = "블록 진행률", example = "50.0")
        @NotNull(message = "블록 진행률은 필수입니다.")
        double blockProgress,

        @Schema(description = "참여 멤버 리스트")
        @NotNull(message = "참여 멤버 리스트는 필수입니다.")
        List<JoinMemberInfoResDto> joinMembers
) {
    public static TeamDashboardInfoResDto of(Member member, TeamDashboard dashboard) {
        return commonBuilder(member, dashboard)
                .build();
    }

    public static TeamDashboardInfoResDto detailOf(Member member, TeamDashboard dashboard, double blockProgress) {
        List<JoinMemberInfoResDto> joinMemberInfoResDtos = new java.util.ArrayList<>();
        joinMemberInfoResDtos.add(JoinMemberInfoResDto.from(dashboard.getMember()));

        joinMemberInfoResDtos.addAll(dashboard.getTeamDashboardMemberMappings().stream()
                .map(mapping -> JoinMemberInfoResDto.from(mapping.getMember()))
                .toList());

        return commonBuilder(member, dashboard)
                .blockProgress(blockProgress)
                .joinMembers(joinMemberInfoResDtos)
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

    @Builder
    @Schema(description = "참여 멤버 정보 DTO")
    private record JoinMemberInfoResDto(
            @Schema(description = "회원 ID", example = "1")
            @NotNull(message = "회원 ID는 필수입니다.")
            Long id,

            @Schema(description = "프로필 사진 URL", example = "http://example.com/profile.jpg")
            @NotNull(message = "프로필 사진 URL은 필수입니다.")
            String picture,

            @Schema(description = "이메일", example = "user@example.com")
            @NotNull(message = "이메일은 필수입니다.")
            String email,

            @Schema(description = "이름", example = "홍길동")
            @NotNull(message = "이름은 필수입니다.")
            String name,

            @Schema(description = "닉네임", example = "길동이")
            @NotNull(message = "닉네임은 필수입니다.")
            String nickName,

            @Schema(description = "소셜 로그인 타입", example = "KAKAO")
            @NotNull(message = "소셜 로그인 타입은 필수입니다.")
            SocialType socialType,

            @Schema(description = "자기소개", example = "안녕하세요.")
            @NotNull(message = "자기소개는 필수입니다.")
            String introduction
    ) {
        private static JoinMemberInfoResDto from(Member member) {
            return JoinMemberInfoResDto.builder()
                    .id(member.getId())
                    .picture(member.getPicture())
                    .email(member.getEmail())
                    .name(member.getName())
                    .nickName(member.getNickname())
                    .socialType(member.getSocialType())
                    .introduction(member.getIntroduction())
                    .build();
        }
    }

}
