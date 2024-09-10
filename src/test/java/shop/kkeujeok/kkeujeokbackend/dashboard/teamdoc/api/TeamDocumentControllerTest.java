package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.requestFields;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.responseFields;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.TokenReqDto;
import shop.kkeujeok.kkeujeokbackend.common.annotation.ControllerTest;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request.FindTeamDocumentReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request.TeamDocumentReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request.TeamDocumentUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response.FindTeamDocumentResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response.TeamDocumentCategoriesResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response.TeamDocumentDetailResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response.TeamDocumentResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.application.TeamDocumentService;
import shop.kkeujeok.kkeujeokbackend.global.annotationresolver.CurrentUserEmailArgumentResolver;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.global.error.ControllerAdvice;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

import java.util.Collections;
import java.util.List;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

import org.springframework.test.web.servlet.MockMvc;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;

class TeamDocumentControllerTest extends ControllerTest {

    @InjectMocks
    private TeamDocumentController teamDocumentController;

    @MockBean
    private TeamDocumentService teamDocumentService;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        ReflectionTestUtils.setField(teamDocumentController, "teamDocumentService", teamDocumentService);

        mockMvc = MockMvcBuilders.standaloneSetup(teamDocumentController)
                .setCustomArgumentResolvers(new CurrentUserEmailArgumentResolver(tokenProvider))
                .apply(documentationConfiguration(restDocumentation))
                .setControllerAdvice(new ControllerAdvice())
                .build();
    }

//    @DisplayName("POST 팀 문서 저장 테스트")
//    @Test
//    void save() throws Exception {
//        // given
//        TeamDocumentReqDto request = new TeamDocumentReqDto("title", "content", "category", 1L);
//        TeamDocumentResDto response = new TeamDocumentResDto(
//                "author",
//                "https://k.kakaocdn.net/dn/bJY1vO/btsJm21aVow/IW9XqFaAdMXFDPvmtQPcK/img_110x110.jpg",
//                "title",
//                "category",
//                9L
//        );
//
//        // Mocking TeamDocumentService
//        given(teamDocumentService.save(anyString(), any(TeamDocumentReqDto.class)))
//                .willReturn(response);
//
//        // when & then
//        mockMvc.perform(post("/api/dashboards/team/document")
//                        .header("Authorization", "Bearer valid-token")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("document/save",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestHeaders(
//                                headerWithName("Authorization").description("JWT 토큰")
//                        ),
//                        requestFields(
//                                fieldWithPath("title").description("문서 제목"),
//                                fieldWithPath("content").description("문서 내용"),
//                                fieldWithPath("category").description("문서 카테고리"),
//                                fieldWithPath("teamDashboardId").description("팀 대시보드 ID")
//                        ),
//                        responseFields(
//                                fieldWithPath("statusCode").description("상태 코드"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("data.author").description("문서 글쓴이"),
//                                fieldWithPath("data.picture").description("문서 글쓴이 사진 URL"),
//                                fieldWithPath("data.title").description("문서 제목"),
//                                fieldWithPath("data.category").description("문서 카테고리"),
//                                fieldWithPath("data.teamDocumentId").description("문서 ID")
//                        )
//                ));
//    }

    @DisplayName("PATCH 팀 문서 수정 테스트")
    @Test
    void update() throws Exception {
        // given
        TeamDocumentUpdateReqDto request = new TeamDocumentUpdateReqDto("11111111", "111111", "호우");
        TeamDocumentResDto response = new TeamDocumentResDto("최인호", "picture", "11111111", "호우", 3L);

        given(teamDocumentService.update(any(), anyLong(), any(TeamDocumentUpdateReqDto.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(patch("/api/dashboards/team/document/{teamDocumentId}", 3L)
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("document/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("teamDocumentId").description("팀 문서 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("문서 제목"),
                                fieldWithPath("content").description("문서 내용"),
                                fieldWithPath("category").description("문서 카테고리")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.author").description("문서 작성자"),
                                fieldWithPath("data.picture").description("문서 작성자의 프로필 사진 URL"),
                                fieldWithPath("data.title").description("문서 제목"),
                                fieldWithPath("data.category").description("문서 카테고리"),
                                fieldWithPath("data.teamDocumentId").description("문서 ID")
                        )
                ));
    }

    @DisplayName("GET 팀 문서 상세 조회 테스트")
    @Test
    void findById() throws Exception {
        // given
        Long teamDocumentId = 1L;
        TeamDocumentDetailResDto response = new TeamDocumentDetailResDto("author", "picture", "title", "content", "category", teamDocumentId);

        given(teamDocumentService.findById(teamDocumentId)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/dashboards/team/document/{teamDocumentId}", teamDocumentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(document("document/findById",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("teamDocumentId").description("팀 문서 ID")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.author").description("문서 작성자"),
                                fieldWithPath("data.picture").description("문서 작성자의 프로필 사진"),
                                fieldWithPath("data.title").description("문서 제목"),
                                fieldWithPath("data.content").description("문서 내용"),
                                fieldWithPath("data.category").description("문서 카테고리"),
                                fieldWithPath("data.teamDocumentId").description("문서 ID")
                        )
                ));
    }

    @DisplayName("GET 팀 문서 카테고리 조회 테스트")
    @Test
    void findCategories() throws Exception {
        // given
        Long teamDashboardId = 1L;
        TeamDocumentCategoriesResDto response = new TeamDocumentCategoriesResDto(List.of("category1", "category2"));

        given(teamDocumentService.findTeamDocumentCategory(teamDashboardId)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/dashboards/team/document/categories/{teamDashboardId}", teamDashboardId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(document("document/findCategories",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("teamDashboardId").description("팀 대시보드 ID")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.categories[]").description("문서 카테고리 리스트")
                        )
                ));
    }

    @DisplayName("GET 팀 문서 카테고리 조회 테스트")
    @Test
    void findTeamDocumentByCategory() throws Exception {
        // given
        List<TeamDocumentResDto> teamDocuments = List.of(
                new TeamDocumentResDto("최인호", "picture", "", "이능", 6L),
                new TeamDocumentResDto("최인호", "picture", "", "이능", 7L)
        );

        PageInfoResDto pageInfoResDto = new PageInfoResDto(0, 1, 2);

        FindTeamDocumentResDto response = FindTeamDocumentResDto.from(teamDocuments, pageInfoResDto);

        given(teamDocumentService.findTeamDocumentByCategory(anyLong(), anyString(), any(PageRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/api/dashboards/team/document/search/{teamDashboardId}", 3L)
                        .param("category", "이능")
                        .param("page", "0")
                        .param("size", "10")
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("document/search-by-category",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("teamDashboardId").description("팀 대시보드 ID")
                        ),
                        queryParameters(
                                parameterWithName("category").description("문서 카테고리"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.teamDocuments[]").description("문서 리스트"),
                                fieldWithPath("data.teamDocuments[].author").description("문서 작성자"),
                                fieldWithPath("data.teamDocuments[].picture").description("문서 작성자의 프로필 사진 URL"),
                                fieldWithPath("data.teamDocuments[].title").description("문서 제목"),
                                fieldWithPath("data.teamDocuments[].category").description("문서 카테고리"),
                                fieldWithPath("data.teamDocuments[].teamDocumentId").description("문서 ID"),
                                fieldWithPath("data.pageInfoResDto.currentPage").description("현재 페이지 번호"),
                                fieldWithPath("data.pageInfoResDto.totalPages").description("전체 페이지 수"),
                                fieldWithPath("data.pageInfoResDto.totalItems").description("전체 아이템 수")
                        )
                ));
    }

    @DisplayName("DELETE 팀 문서 삭제 테스트")
    @Test
    void deleteTeamDocument() throws Exception {
        // given
        doNothing().when(teamDocumentService).delete(anyLong());

        // when & then
        mockMvc.perform(delete("/api/dashboards/team/document/{teamDocumentId}", 1L)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("document/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("teamDocumentId").description("팀 문서 ID")
                        )
                ));
    }
}