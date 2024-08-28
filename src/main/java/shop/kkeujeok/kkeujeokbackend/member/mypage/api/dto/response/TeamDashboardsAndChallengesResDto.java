package shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response;

import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardListResDto;

public record TeamDashboardsAndChallengesResDto(
        TeamDashboardListResDto teamDashboardList,
        ChallengeListResDto challengeList
) {
    public static TeamDashboardsAndChallengesResDto of(TeamDashboardListResDto teamDashboardList, ChallengeListResDto challengeList) {
        return new TeamDashboardsAndChallengesResDto(
                teamDashboardList,
                challengeList
        );
    }
}
