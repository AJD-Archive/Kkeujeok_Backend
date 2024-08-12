package shop.kkeujeok.kkeujeokbackend.dashboard.team.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request.TeamDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.repository.TeamDashboardRepository;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.exception.MemberNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamDashboardService {

    private final TeamDashboardRepository teamDashboardRepository;
    private final MemberRepository memberRepository;

    // 팀 대시보드 저장
    @Transactional
    public TeamDashboardInfoResDto save(String email, TeamDashboardSaveReqDto teamDashboardSaveReqDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        TeamDashboard teamDashboard = teamDashboardSaveReqDto.toEntity(member);

        teamDashboardRepository.save(teamDashboard);

        return TeamDashboardInfoResDto.of(member, teamDashboard);
    }

}
