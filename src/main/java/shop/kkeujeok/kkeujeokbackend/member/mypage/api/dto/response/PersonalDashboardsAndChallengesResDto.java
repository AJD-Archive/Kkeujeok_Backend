package shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response;

import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardPageListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardListResDto;

public record PersonalDashboardsAndChallengesResDto(
        PersonalDashboardPageListResDto personalDashboardList,
        ChallengeListResDto challengeList
) {
    public static PersonalDashboardsAndChallengesResDto of(PersonalDashboardPageListResDto personalDashboardList,
                                                           ChallengeListResDto challengeList) {
        return new PersonalDashboardsAndChallengesResDto(
                personalDashboardList,
                challengeList
        );
    }
}
