package shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardPageListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardListResDto;

@Schema(description = "팀 대시보드 및 챌린지 정보 응답 DTO")
public record TeamDashboardsAndChallengesResDto(
        @Schema(description = "개인 대시보드 목록")
        PersonalDashboardPageListResDto personalDashboardList,

        @Schema(description = "팀 대시보드 목록")
        TeamDashboardListResDto teamDashboardList,

        @Schema(description = "챌린지 목록")
        ChallengeListResDto challengeList
) {
    public static TeamDashboardsAndChallengesResDto of(PersonalDashboardPageListResDto personalDashboardList,
                                                       TeamDashboardListResDto teamDashboardList,
                                                       ChallengeListResDto challengeList) {
        return new TeamDashboardsAndChallengesResDto(
                personalDashboardList,
                teamDashboardList,
                challengeList
        );
    }
}
