package shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response;

import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;

@Builder
public record TeamDashboardInfoResDto(
        Long dashboardId,
        Long myId,
        Long creatorId,
        String title,
        String description,
        double blockProgress,
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
    private record JoinMemberInfoResDto(
            String picture,
            String email,
            String name,
            String nickName,
            SocialType socialType,
            String introduction
    ) {
        private static JoinMemberInfoResDto from(Member member) {
            return JoinMemberInfoResDto.builder()
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
