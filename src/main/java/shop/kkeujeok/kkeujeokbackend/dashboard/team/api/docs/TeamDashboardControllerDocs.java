package shop.kkeujeok.kkeujeokbackend.dashboard.team.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request.TeamDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request.TeamDashboardUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.SearchMemberListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardListResDto;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

@Tag(name = "TeamDashboardController", description = "팀 대시보드 관련 API")
public interface TeamDashboardControllerDocs {

    @Operation(summary = "팀 대시보드 생성", description = "새로운 팀 대시보드를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 대시보드 생성 성공")
    })
    RspTemplate<TeamDashboardInfoResDto> save(@CurrentUserEmail String email,
                                              @RequestBody @Valid TeamDashboardSaveReqDto teamDashboardSaveReqDto);

    @Operation(summary = "팀 대시보드 수정", description = "기존 팀 대시보드를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 대시보드 수정 성공")
    })
    RspTemplate<TeamDashboardInfoResDto> update(@CurrentUserEmail String email,
                                                @PathVariable(name = "dashboardId") Long dashboardId,
                                                @RequestBody @Valid TeamDashboardUpdateReqDto teamDashboardUpdateReqDto);

    @Operation(summary = "팀 대시보드 전체 조회", description = "사용자가 속한 모든 팀 대시보드를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 대시보드 전체 조회 성공")
    })
    RspTemplate<TeamDashboardListResDto> findForTeamDashboard(@CurrentUserEmail String email);

    @Operation(summary = "팀 대시보드 상세보기", description = "특정 팀 대시보드의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 대시보드 상세보기 성공")
    })
    RspTemplate<TeamDashboardInfoResDto> findById(@CurrentUserEmail String email,
                                                  @PathVariable(name = "dashboardId") Long dashboardId);

    @Operation(summary = "팀 대시보드 삭제/복구", description = "특정 팀 대시보드를 삭제하거나 복구합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 대시보드 삭제/복구 성공")
    })
    RspTemplate<Void> delete(@CurrentUserEmail String email,
                             @PathVariable(name = "dashboardId") Long dashboardId);

    @Operation(summary = "팀 가입", description = "특정 팀 대시보드에 가입합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 가입 성공")
    })
    RspTemplate<Void> joinTeam(@CurrentUserEmail String email,
                               @PathVariable(name = "dashboardId") Long dashboardId);

    @Operation(summary = "팀 탈퇴", description = "특정 팀 대시보드에서 탈퇴합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 탈퇴 성공")
    })
    RspTemplate<Void> leaveTeam(@CurrentUserEmail String email,
                                @PathVariable(name = "dashboardId") Long dashboardId);

    @Operation(summary = "팀원 초대 리스트 검색", description = "팀에 초대할 회원을 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀원 초대 리스트 검색 성공")
    })
    RspTemplate<SearchMemberListResDto> search(@RequestParam(name = "query") String query);
}
