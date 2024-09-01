package shop.kkeujeok.kkeujeokbackend.dashboard.team.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.requestFields;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.responseFields;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.TokenReqDto;
import shop.kkeujeok.kkeujeokbackend.common.annotation.ControllerTest;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request.TeamDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request.TeamDashboardUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.SearchMemberListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.global.annotationresolver.CurrentUserEmailArgumentResolver;
import shop.kkeujeok.kkeujeokbackend.global.error.ControllerAdvice;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;

class TeamDashboardControllerTest extends ControllerTest {

    private Member member;
    private Member joinMember;
    private TeamDashboard teamDashboard;
    private TeamDashboardSaveReqDto teamDashboardSaveReqDto;
    private TeamDashboardUpdateReqDto teamDashboardUpdateReqDto;

    @InjectMocks
    private TeamDashboardController teamDashboardController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        member = Member.builder()
                .email("email")
                .name("name")
                .nickname("nickname")
                .socialType(SocialType.GOOGLE)
                .introduction("introduction")
                .picture("picture")
                .build();

        joinMember = Member.builder()
                .email("joinEmail")
                .name("joinName")
                .nickname("joinNickname")
                .socialType(SocialType.GOOGLE)
                .introduction("joinIntroduction")
                .picture("joinPicture")
                .build();

        teamDashboardSaveReqDto = new TeamDashboardSaveReqDto("title", "description");
        teamDashboardUpdateReqDto = new TeamDashboardUpdateReqDto("updateTitle", "updateDescription");
        teamDashboard = teamDashboardSaveReqDto.toEntity(member);

        ReflectionTestUtils.setField(teamDashboard, "id", 1L);
        ReflectionTestUtils.setField(member, "id", 1L);
        ReflectionTestUtils.setField(teamDashboard, "member", member);

        teamDashboardController = new TeamDashboardController(teamDashboardService);

        mockMvc = MockMvcBuilders.standaloneSetup(teamDashboardController)
                .apply(documentationConfiguration(restDocumentation))
                .setCustomArgumentResolvers(new CurrentUserEmailArgumentResolver(tokenProvider))
                .setControllerAdvice(new ControllerAdvice())
                .build();

        when(tokenProvider.getUserEmailFromToken(any(TokenReqDto.class))).thenReturn("email");
    }

    @DisplayName("POST 팀 대시보드 저장 컨트롤러 로직 확인")
    @Test
    void 팀_대시보드_저장() throws Exception {
        // given
        TeamDashboardInfoResDto response = TeamDashboardInfoResDto.of(member, teamDashboard);
        given(teamDashboardService.save(anyString(), any(TeamDashboardSaveReqDto.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/dashboards/team/")
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamDashboardSaveReqDto)))
                .andDo(print())
                .andDo(document("dashboard/team/save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("title").description("팀 대시보드 제목"),
                                fieldWithPath("description").description("팀 대시보드 설명")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.dashboardId").description("대시보드 아이디"),
                                fieldWithPath("data.myId").description("내 아이디"),
                                fieldWithPath("data.creatorId").description("팀 대시보드 생성자 아이디"),
                                fieldWithPath("data.title").description("팀 대시보드 제목"),
                                fieldWithPath("data.description").description("팀 대시보드 설명"),
                                fieldWithPath("data.blockProgress").description("팀 대시보드의 완료된 블록 진행률"),
                                fieldWithPath("data.joinMembers").description("팀 대시보드에 참여한 사용자")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("PATCH 팀 대시보드 수정 컨트롤러 로직 확인")
    @Test
    void 팀_대시보드_수정() throws Exception {
        // given
        teamDashboard.update(teamDashboardUpdateReqDto.title(), teamDashboardUpdateReqDto.description());
        TeamDashboardInfoResDto response = TeamDashboardInfoResDto.of(member, teamDashboard);
        given(teamDashboardService.update(anyString(), anyLong(), any(TeamDashboardUpdateReqDto.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(patch("/api/dashboards/team/{dashboardId}", 1L)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamDashboardUpdateReqDto)))
                .andDo(print())
                .andDo(document("dashboard/team/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        pathParameters(
                                parameterWithName("dashboardId").description("대시보드 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("개인 대시보드 제목"),
                                fieldWithPath("description").description("개인 대시보드 설명")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.dashboardId").description("대시보드 아이디"),
                                fieldWithPath("data.myId").description("내 아이디"),
                                fieldWithPath("data.creatorId").description("팀 대시보드 생성자 아이디"),
                                fieldWithPath("data.title").description("팀 대시보드 제목"),
                                fieldWithPath("data.description").description("팀 대시보드 설명"),
                                fieldWithPath("data.blockProgress").description("팀 대시보드의 완료된 블록 진행률"),
                                fieldWithPath("data.joinMembers").description("팀 대시보드에 참여한 사용자")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("GET 팀 대시보드를 전체 조회합니다.")
    @Test
    void 팀_대시보드_전체_조회() throws Exception {
        // given
        TeamDashboardListResDto response = TeamDashboardListResDto.from(
                Collections.singletonList(TeamDashboardInfoResDto.of(member, teamDashboard))
        );

        given(teamDashboardService.findForTeamDashboard(anyString())).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/dashboards/team/")
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("dashboard/team/findForTeamDashboard",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.teamDashboardInfoResDto[].dashboardId")
                                        .description("대시보드 아이디"),
                                fieldWithPath("data.teamDashboardInfoResDto[].myId")
                                        .description("내 아이디"),
                                fieldWithPath("data.teamDashboardInfoResDto[].creatorId")
                                        .description("팀 대시보드 생성자 아이디"),
                                fieldWithPath("data.teamDashboardInfoResDto[].title")
                                        .description("팀 대시보드 제목"),
                                fieldWithPath("data.teamDashboardInfoResDto[].description")
                                        .description("팀 대시보드 설명"),
                                fieldWithPath("data.teamDashboardInfoResDto[].blockProgress")
                                        .description("팀 대시보드의 완료된 블록 진행률"),
                                fieldWithPath("data.teamDashboardInfoResDto[].joinMembers")
                                        .description("팀 대시보드에 참여한 사용자"),
                                fieldWithPath("data.pageInfoResDto")
                                        .description("페이지 정보가 없습니다.")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("GET 팀 대시보드를 상세봅니다.")
    @Test
    void 팀_대시보드_상세보기() throws Exception {
        // given
        TeamDashboardInfoResDto response = TeamDashboardInfoResDto.of(member, teamDashboard);

        given(teamDashboardService.findById(anyString(), anyLong())).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/dashboards/team/{dashboardId}", 1L)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("dashboard/team/findById",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        pathParameters(
                                parameterWithName("dashboardId").description("대시보드 아이디")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.dashboardId").description("대시보드 아이디"),
                                fieldWithPath("data.myId").description("내 아이디"),
                                fieldWithPath("data.creatorId").description("팀 대시보드 생성자 아이디"),
                                fieldWithPath("data.title").description("팀 대시보드 제목"),
                                fieldWithPath("data.description").description("팀 대시보드 설명"),
                                fieldWithPath("data.blockProgress").description("팀 대시보드의 완료된 블록 진행률"),
                                fieldWithPath("data.joinMembers").description("팀 대시보드에 참여한 사용자")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("DELETE 팀 대시보드를 논리적으로 삭제하고 복구합니다.")
    @Test
    void 팀_대시보드_삭제() throws Exception {
        // given
        doNothing().when(teamDashboardService).delete(anyString(), anyLong());

        // when & then
        mockMvc.perform(delete("/api/dashboards/team/{dashboardId}", 1L)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("dashboard/team/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        pathParameters(
                                parameterWithName("dashboardId").description("팀 대시보드 ID")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("POST 팀 대시보드 초대를 수락합니다.")
    @Test
    void 팀_대시보드_초대_수락() throws Exception {
        // given
        doNothing().when(teamDashboardService).joinTeam(anyString(), anyLong());

        // when & then
        mockMvc.perform(post("/api/dashboards/team/{dashboardId}/join", 1L)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("dashboard/team/join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        pathParameters(
                                parameterWithName("dashboardId").description("팀 대시보드 ID")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("POST 참여한 팀 대시보드를 탈퇴합니다.")
    @Test
    void 팀_대시보드_탈퇴() throws Exception {
        // given
        doNothing().when(teamDashboardService).leaveTeam(anyString(), anyLong());

        // when & then
        mockMvc.perform(post("/api/dashboards/team/{dashboardId}/leave", 1L)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("dashboard/team/leave",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        pathParameters(
                                parameterWithName("dashboardId").description("팀 대시보드 ID")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("GET 팀원 초대 리스트를 조회합니다.")
    @Test
    void 팀_초대_멤버_조회() throws Exception {
        // given
        SearchMemberListResDto response = SearchMemberListResDto.from(List.of(joinMember));

        given(teamDashboardService.searchMembers(anyString())).willReturn(response);

        // when & then
        mockMvc.perform(get(String.format("/api/dashboards/team/search?query=%s", "joinEmail"))
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("dashboard/team/search",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        queryParameters(
                                parameterWithName("query").description("검색 조건(이메일, 닉네임#고유번호)")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.searchMembers[].id").description("대시보드 아이디"),
                                fieldWithPath("data.searchMembers[].picture").description("내 아이디"),
                                fieldWithPath("data.searchMembers[].email").description("팀 대시보드 생성자 아이디")
                        )
                ))
                .andExpect(status().isOk());
    }
}