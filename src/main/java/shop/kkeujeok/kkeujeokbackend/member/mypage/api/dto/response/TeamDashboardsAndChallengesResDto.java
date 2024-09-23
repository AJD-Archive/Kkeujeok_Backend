package shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response;

import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardPageListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardListResDto;

public record TeamDashboardsAndChallengesResDto(
        PersonalDashboardPageListResDto personalDashboardList,
        TeamDashboardListResDto teamDashboardList,
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
