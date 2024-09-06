package shop.kkeujeok.kkeujeokbackend.dashboard.team.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request.TeamDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request.TeamDashboardUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.SearchMemberListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.application.TeamDashboardService;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboards/team")
public class TeamDashboardController {

    private final TeamDashboardService teamDashboardService;

    @PostMapping("/")
    public RspTemplate<TeamDashboardInfoResDto> save(@CurrentUserEmail String email,
                                                     @RequestBody @Valid TeamDashboardSaveReqDto teamDashboardSaveReqDto) {
        return new RspTemplate<>(HttpStatus.OK,
                "팀 대시보드 생성",
                teamDashboardService.save(email, teamDashboardSaveReqDto));
    }

    @PatchMapping("/{dashboardId}")
    public RspTemplate<TeamDashboardInfoResDto> update(@CurrentUserEmail String email,
                                                       @PathVariable(name = "dashboardId") Long dashboardId,
                                                       @RequestBody @Valid TeamDashboardUpdateReqDto teamDashboardUpdateReqDto) {
        return new RspTemplate<>(HttpStatus.OK,
                "팀 대시보드 수정",
                teamDashboardService.update(email, dashboardId, teamDashboardUpdateReqDto));
    }

    @GetMapping("/")
    public RspTemplate<TeamDashboardListResDto> findForTeamDashboard(@CurrentUserEmail String email,
                                                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                                                     @RequestParam(name = "size", defaultValue = "10") int size) {
        return new RspTemplate<>(HttpStatus.OK,
                "팀 대시보드 전체 조회",
                teamDashboardService.findForTeamDashboard(email, PageRequest.of(page, size)));
    }

    @GetMapping("/{dashboardId}")
    public RspTemplate<TeamDashboardInfoResDto> findById(@CurrentUserEmail String email,
                                                         @PathVariable(name = "dashboardId") Long dashboardId) {
        return new RspTemplate<>(HttpStatus.OK, "팀 대시보드 상세보기", teamDashboardService.findById(email, dashboardId));
    }

    @DeleteMapping("/{dashboardId}")
    public RspTemplate<Void> delete(@CurrentUserEmail String email,
                                    @PathVariable(name = "dashboardId") Long dashboardId) {
        teamDashboardService.delete(email, dashboardId);
        return new RspTemplate<>(HttpStatus.OK, "팀 대시보드 삭제, 복구");
    }

    @PostMapping("/{dashboardId}/join")
    public RspTemplate<Void> joinTeam(@CurrentUserEmail String email,
                                      @PathVariable(name = "dashboardId") Long dashboardId) {
        teamDashboardService.joinTeam(email, dashboardId);
        return new RspTemplate<>(HttpStatus.OK, "팀 가입");
    }

    @PostMapping("/{dashboardId}/leave")
    public RspTemplate<Void> leaveTeam(@CurrentUserEmail String email,
                                       @PathVariable(name = "dashboardId") Long dashboardId) {
        teamDashboardService.leaveTeam(email, dashboardId);
        return new RspTemplate<>(HttpStatus.OK, "팀 탈퇴");
    }

    @GetMapping("/search")
    public RspTemplate<SearchMemberListResDto> search(@RequestParam(name = "query") String query) {
        return new RspTemplate<>(HttpStatus.OK, "팀원 초대 리스트", teamDashboardService.searchMembers(query));
    }

    @GetMapping("/{dashboardId}/invite")
    public RspTemplate<Void> inviteMember(@CurrentUserEmail String inviteSendMemberEmail,
                                          @RequestParam("email") String inviteReceivedMemberEmail,
                                          @PathVariable(name = "dashboardId") Long dashboardId) {
        teamDashboardService.inviteMember(inviteSendMemberEmail, inviteReceivedMemberEmail, dashboardId);

        return new RspTemplate<>(HttpStatus.OK, "팀 대시보드 초대");
    }

}
