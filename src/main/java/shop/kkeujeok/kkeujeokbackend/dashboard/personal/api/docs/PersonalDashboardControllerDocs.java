package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardCategoriesResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardListResDto;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

@Tag(name = "PersonalDashboardController", description = "개인 대시보드 관련 API")
public interface PersonalDashboardControllerDocs {

    @Operation(summary = "개인 대시보드 생성", description = "새로운 개인 대시보드를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "개인 대시보드 생성 성공")
    })
    RspTemplate<PersonalDashboardInfoResDto> save(@CurrentUserEmail String email,
                                                  @RequestBody @Valid PersonalDashboardSaveReqDto personalDashboardSaveReqDto);

    @Operation(summary = "개인 대시보드 수정", description = "기존 개인 대시보드를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "개인 대시보드 수정 성공")
    })
    RspTemplate<PersonalDashboardInfoResDto> update(@CurrentUserEmail String email,
                                                    @PathVariable(name = "dashboardId") Long dashboardId,
                                                    @RequestBody PersonalDashboardUpdateReqDto personalDashboardUpdateReqDto);

    @Operation(summary = "개인 대시보드 전체 조회", description = "사용자의 모든 개인 대시보드를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "개인 대시보드 전체 조회 성공")
    })
    RspTemplate<PersonalDashboardListResDto> findForPersonalDashboard(@CurrentUserEmail String email);

    @Operation(summary = "개인 대시보드 상세보기", description = "특정 개인 대시보드의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "개인 대시보드 상세보기 성공")
    })
    RspTemplate<PersonalDashboardInfoResDto> findById(@CurrentUserEmail String email,
                                                      @PathVariable(name = "dashboardId") Long dashboardId);

    @Operation(summary = "개인 대시보드 카테고리 조회", description = "사용자의 개인 대시보드 카테고리 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "개인 대시보드 카테고리 조회 성공")
    })
    RspTemplate<PersonalDashboardCategoriesResDto> findCategoriesForDashboard(@CurrentUserEmail String email);

    @Operation(summary = "개인 대시보드 삭제/복구", description = "특정 개인 대시보드를 삭제하거나 복구합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "개인 대시보드 삭제/복구 성공")
    })
    RspTemplate<Void> delete(@CurrentUserEmail String email,
                             @PathVariable(name = "dashboardId") Long dashboardId);
}
