package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardCategoriesResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.application.PersonalDashboardService;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboards/personal")
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

    @GetMapping("/")
    public RspTemplate<PersonalDashboardListResDto> findForPersonalDashboard(@CurrentUserEmail String email) {
        return new RspTemplate<>(HttpStatus.OK,
                "개인 대시보드 전체 조회",
                personalDashboardService.findForPersonalDashboard(email));
    }

    @GetMapping("/{dashboardId}")
    public RspTemplate<PersonalDashboardInfoResDto> findById(@CurrentUserEmail String email,
                                                             @PathVariable(name = "dashboardId") Long dashboardId) {
        return new RspTemplate<>(HttpStatus.OK, "개인 대시보드 상세보기", personalDashboardService.findById(email, dashboardId));
    }

    @GetMapping("/categories")
    public RspTemplate<PersonalDashboardCategoriesResDto> findCategoriesForDashboard(@CurrentUserEmail String email) {
        return new RspTemplate<>(HttpStatus.OK,
                "개인 대시보드 카테고리 조회",
                personalDashboardService.findCategoriesForDashboard(email));
    }

    @DeleteMapping("/{dashboardId}")
    public RspTemplate<Void> delete(@CurrentUserEmail String email,
                                    @PathVariable(name = "dashboardId") Long dashboardId) {
        personalDashboardService.delete(email, dashboardId);
        return new RspTemplate<>(HttpStatus.OK, "개인 대시보드 삭제, 복구");
    }

}
