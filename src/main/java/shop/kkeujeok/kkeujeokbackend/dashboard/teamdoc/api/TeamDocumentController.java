package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardCategoriesResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request.FindTeamDocumentReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request.TeamDocumentReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request.TeamDocumentUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response.FindTeamDocumentResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response.TeamDocumentCategoriesResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response.TeamDocumentResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.application.TeamDocumentService;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/dashboards/team/document")
public class TeamDocumentController {

    private final TeamDocumentService teamDocumentService;

    @PostMapping("")
    public RspTemplate<TeamDocumentResDto> save(@CurrentUserEmail String email,
                                                @RequestBody TeamDocumentReqDto documentReqDto) {
        return new RspTemplate<>(HttpStatus.OK,
                "팀 문서 생성",
                teamDocumentService.save(email,documentReqDto));
    }

    @PatchMapping("/{teamDocumentId}")
    public RspTemplate<TeamDocumentResDto> update(@CurrentUserEmail String email,
                                                  @PathVariable(name = "teamDocumentId") Long teamDocumentId,
                                                  @RequestBody TeamDocumentUpdateReqDto teamDocumentUpdateReqDto) {
        return new RspTemplate<>(HttpStatus.OK, "팀 문서 수정", teamDocumentService.update(email, teamDocumentId, teamDocumentUpdateReqDto));
    }

    @GetMapping("/categories/{teamDashboardId}")
    public RspTemplate<TeamDocumentCategoriesResDto> findCategories(@PathVariable(name = "teamDashboardId") Long teamDashboardId) {
        return new RspTemplate<>(HttpStatus.OK,
                "팀 문서 카테고리 조회",
                teamDocumentService.findTeamDocumentCategory(teamDashboardId));
    }

    // 카테고리 선택해서 그걸로 찾기(쿼리)
    @GetMapping("/search/{teamDashboardId}")
    public RspTemplate<FindTeamDocumentResDto> findTeamDocumentByCategory(@PathVariable(name = "teamDashboardId") Long teamDashboardId,
                                                                          @RequestParam(name = "category") String category,
                                                                          @RequestParam(defaultValue = "0", name = "page") int page,
                                                                          @RequestParam(defaultValue = "10", name = "size") int size){
        return new RspTemplate<>(HttpStatus.OK,
                "팀 문서 카테고리로 조회",
                teamDocumentService.findTeamDocumentByCategory(teamDashboardId,
                        FindTeamDocumentReqDto.of(category),
                        PageRequest.of(page, size)));
    }

    // 팀 문서 삭제
    @DeleteMapping("/{teamDocumentId}")
    public RspTemplate<Void> delete(@PathVariable(name = "teamDocumentId") Long teamDocumentId) {
        teamDocumentService.delete(teamDocumentId);

        return new RspTemplate<>(HttpStatus.OK, "팀 문서 삭제");
    }
}
