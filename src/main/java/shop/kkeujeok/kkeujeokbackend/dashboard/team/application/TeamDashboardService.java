package shop.kkeujeok.kkeujeokbackend.dashboard.team.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.dashboard.exception.DashboardNotFoundException;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.exception.DashboardAccessDeniedException;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request.TeamDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request.TeamDashboardUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.SearchMemberListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.repository.TeamDashboardRepository;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
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

    // 팀 대시보드 수정
    @Transactional
    public TeamDashboardInfoResDto update(String email,
                                          Long dashboardId,
                                          TeamDashboardUpdateReqDto teamDashboardUpdateReqDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        TeamDashboard dashboard = teamDashboardRepository.findById(dashboardId)
                .orElseThrow(DashboardNotFoundException::new);

        verifyMemberIsAuthor(dashboard, member);

        dashboard.update(teamDashboardUpdateReqDto.title(),
                teamDashboardUpdateReqDto.description());

        return TeamDashboardInfoResDto.of(member, dashboard);
    }

    // 팀 대시보드 전체 조회(페이지네이션)
    public TeamDashboardListResDto findForTeamDashboard(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Page<TeamDashboard> teamDashboards = teamDashboardRepository.findForTeamDashboard(member, pageable);

        List<TeamDashboardInfoResDto> teamDashboardInfoResDtoList = teamDashboards.stream()
                .map(t -> TeamDashboardInfoResDto.of(member, t))
                .toList();

        return TeamDashboardListResDto
                .of(teamDashboardInfoResDtoList, PageInfoResDto.from(teamDashboards));
    }

    // 팀 대시보드 전체 조회(페이지네이션 X)
    public TeamDashboardListResDto findForTeamDashboard(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        List<TeamDashboard> teamDashboards = teamDashboardRepository.findForTeamDashboard(member);

        List<TeamDashboardInfoResDto> teamDashboardInfoResDtoList = teamDashboards.stream()
                .map(t -> TeamDashboardInfoResDto.of(member, t))
                .toList();

        return TeamDashboardListResDto.from(teamDashboardInfoResDtoList);
    }

    // 팀 대시보드 상세 조회
    public TeamDashboardInfoResDto findById(String email, Long dashboardId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        TeamDashboard dashboard = teamDashboardRepository.findById(dashboardId)
                .orElseThrow(DashboardNotFoundException::new);

        double blockProgress = teamDashboardRepository.calculateCompletionPercentage(dashboard.getId());

        return TeamDashboardInfoResDto.detailOf(member, dashboard, blockProgress);
    }

    // 팀 대시보드 삭제 유무 업데이트 (논리 삭제)
    @Transactional
    public void delete(String email, Long dashboardId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        TeamDashboard dashboard = teamDashboardRepository.findById(dashboardId)
                .orElseThrow(DashboardNotFoundException::new);

        verifyMemberIsAuthor(dashboard, member);

        dashboard.statusUpdate();
    }

    @Transactional
    public void joinTeam(String email, Long dashboardId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        TeamDashboard dashboard = teamDashboardRepository.findById(dashboardId)
                .orElseThrow(DashboardNotFoundException::new);

        dashboard.addMember(member);
    }

    @Transactional
    public void leaveTeam(String email, Long dashboardId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        TeamDashboard dashboard = teamDashboardRepository.findById(dashboardId)
                .orElseThrow(DashboardNotFoundException::new);

        dashboard.removeMember(member);
    }

    public SearchMemberListResDto searchMembers(String query) {
        List<Member> searchMembers = teamDashboardRepository.findForMembersByQuery(query);

        return SearchMemberListResDto.from(searchMembers);
    }

    private void verifyMemberIsAuthor(TeamDashboard teamDashboard, Member member) {
        if (!member.equals(teamDashboard.getMember())) {
            throw new DashboardAccessDeniedException();
        }
    }
}
