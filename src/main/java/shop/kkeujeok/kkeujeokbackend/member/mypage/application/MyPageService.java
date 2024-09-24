package shop.kkeujeok.kkeujeokbackend.member.mypage.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.auth.exception.EmailNotFoundException;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeListResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.application.ChallengeService;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardPageListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.application.PersonalDashboardService;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.application.TeamDashboardService;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.request.MyPageUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.MyPageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.TeamDashboardsAndChallengesResDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.exception.ExistsNicknameException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final MemberRepository memberRepository;
    private final PersonalDashboardService personalDashboardService;
    private final TeamDashboardService teamDashboardService;
    private final ChallengeService challengeService;

    // 프로필 정보 조회
    public MyPageInfoResDto findMyProfileByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();

        return MyPageInfoResDto.From(member);
    }

    // 프로필 정보 수정
    @Transactional
    public MyPageInfoResDto update(String email, MyPageUpdateReqDto myPageUpdateReqDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(EmailNotFoundException::new);

        if (isNicknameChanged(member, myPageUpdateReqDto.nickname()) && isNicknameDuplicate(myPageUpdateReqDto.nickname())) {
            throw new ExistsNicknameException();
        }

        member.update(myPageUpdateReqDto.nickname(), myPageUpdateReqDto.introduction());

        return MyPageInfoResDto.From(member);
    }

    // 대시보드 & 챌린지 정보 조회
    @Transactional(readOnly = true)
    public TeamDashboardsAndChallengesResDto findTeamDashboardsAndChallenges(String email,
                                                                             String requestEmail,
                                                                             Pageable pageable) {
        TeamDashboardListResDto teamDashboardListResDto = teamDashboardService.findForTeamDashboard(requestEmail, pageable);
        ChallengeListResDto challengeListResDto = challengeService.findChallengeForMemberId(requestEmail, pageable);

        PersonalDashboardPageListResDto personalDashboardPageListResDto;

        if (email.equals(requestEmail)) {
            // 본인이면 isPublic과 상관없이 모든 대시보드 및 챌린지 조회
            personalDashboardPageListResDto = personalDashboardService.findForPersonalDashboard(requestEmail, pageable);
        } else {
            // 타인이면 isPublic이 true인 대시보드 및 챌린지 조회
            personalDashboardPageListResDto = personalDashboardService.findPublicPersonalDashboards(requestEmail, pageable);
        }
        return TeamDashboardsAndChallengesResDto.of(personalDashboardPageListResDto,teamDashboardListResDto, challengeListResDto);
    }

    private boolean isNicknameChanged(Member member, String newNickname) {
        return !normalizeNickname(member.getNickname()).equals(normalizeNickname(newNickname));
    }

    private boolean isNicknameDuplicate(String nickname) {
        return memberRepository.existsByNickname(normalizeNickname(nickname));
    }

    private String normalizeNickname(String nickname) {
        return nickname.replaceAll("\\s+", "");
    }
}
