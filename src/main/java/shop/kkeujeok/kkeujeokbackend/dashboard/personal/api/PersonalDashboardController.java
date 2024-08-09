package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.application.PersonalDashboardService;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboards")
public class PersonalDashboardController {

    private final PersonalDashboardService personalDashboardService;

    @PostMapping("/")
    public RspTemplate<PersonalDashboardInfoResDto> save(@CurrentUserEmail String email,
                                                         @RequestBody @Valid PersonalDashboardSaveReqDto personalDashboardSaveReqDto) {
        return new RspTemplate<>(HttpStatus.OK,
                "개인 대시보드 생성",
                personalDashboardService.save(email, personalDashboardSaveReqDto));
    }

    @PatchMapping("/{dashboardId}")
    public RspTemplate<PersonalDashboardInfoResDto> update(@CurrentUserEmail String email,
                                                           @PathVariable(name = "dashboardId") Long dashboardId,
                                                           @RequestBody PersonalDashboardUpdateReqDto personalDashboardUpdateReqDto) {
        return new RspTemplate<>(HttpStatus.OK,
                "개인 대시보드 수정",
                personalDashboardService.update(email, dashboardId, personalDashboardUpdateReqDto));
    }

}
