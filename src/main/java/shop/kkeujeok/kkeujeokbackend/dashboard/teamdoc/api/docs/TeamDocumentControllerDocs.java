package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request.TeamDocumentReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request.TeamDocumentUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response.FindTeamDocumentResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response.TeamDocumentCategoriesResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response.TeamDocumentDetailResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response.TeamDocumentResDto;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

@Tag(name = "TeamDocumentController", description = "팀 문서 관련 API")
public interface TeamDocumentControllerDocs {

    @Operation(summary = "팀 문서 생성", description = "새로운 팀 문서를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 문서 생성 성공")
    })
    RspTemplate<TeamDocumentResDto> save(@CurrentUserEmail String email,
                                         @RequestBody TeamDocumentReqDto documentReqDto);

    @Operation(summary = "팀 문서 수정", description = "기존 팀 문서를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 문서 수정 성공")
    })
    RspTemplate<TeamDocumentResDto> update(@CurrentUserEmail String email,
                                           @PathVariable(name = "teamDocumentId") Long teamDocumentId,
                                           @RequestBody TeamDocumentUpdateReqDto teamDocumentUpdateReqDto);

    @Operation(summary = "팀 문서 상세보기", description = "특정 팀 문서의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 문서 상세보기 성공")
    })
    RspTemplate<TeamDocumentDetailResDto> findById(@PathVariable(name = "teamDocumentId") Long teamDocumentId);

    @Operation(summary = "팀 문서 카테고리 조회", description = "특정 팀 대시보드의 문서 카테고리 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 문서 카테고리 조회 성공")
    })
    RspTemplate<TeamDocumentCategoriesResDto> findCategories(@PathVariable(name = "teamDashboardId") Long teamDashboardId);

    @Operation(summary = "팀 문서 카테고리로 조회", description = "특정 카테고리에 속한 팀 문서를 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 문서 카테고리로 조회 성공")
    })
    RspTemplate<FindTeamDocumentResDto> findTeamDocumentByCategory(@PathVariable(name = "teamDashboardId") Long teamDashboardId,
                                                                   @RequestParam(defaultValue = "", name = "category") String category,
                                                                   @RequestParam(defaultValue = "0", name = "page") int page,
                                                                   @RequestParam(defaultValue = "10", name = "size") int size);

    @Operation(summary = "팀 문서 삭제", description = "특정 팀 문서를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 문서 삭제 성공")
    })
    RspTemplate<Void> delete(@PathVariable(name = "teamDocumentId") Long teamDocumentId);
}
