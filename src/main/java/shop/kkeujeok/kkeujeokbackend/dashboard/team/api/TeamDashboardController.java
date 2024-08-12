package shop.kkeujeok.kkeujeokbackend.dashboard.team.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request.TeamDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request.TeamDashboardUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardInfoResDto;
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
                                                       @PathVariable Long dashboardId,
                                                       @RequestBody @Valid TeamDashboardUpdateReqDto teamDashboardUpdateReqDto) {
        return new RspTemplate<>(HttpStatus.OK,
                "팀 대시보드 수정",
                teamDashboardService.update(email, dashboardId, teamDashboardUpdateReqDto));
    }

}
