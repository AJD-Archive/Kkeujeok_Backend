package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.requestFields;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.responseFields;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.TokenReqDto;
import shop.kkeujeok.kkeujeokbackend.common.annotation.ControllerTest;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.global.annotationresolver.CurrentUserEmailArgumentResolver;
import shop.kkeujeok.kkeujeokbackend.global.error.ControllerAdvice;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;

class PersonalDashboardControllerTest extends ControllerTest {

    private Member member;
    private PersonalDashboard personalDashboard;
    private PersonalDashboardSaveReqDto personalDashboardSaveReqDto;
    private PersonalDashboardUpdateReqDto personalDashboardUpdateReqDto;

    @InjectMocks
    private PersonalDashboardController personalDashboardController;

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

        personalDashboardSaveReqDto = new PersonalDashboardSaveReqDto("title", "description", false, "category");
        personalDashboardUpdateReqDto = new PersonalDashboardUpdateReqDto("updateTitle", "updateDescription", true,
                "updateCategory");
        personalDashboard = personalDashboardSaveReqDto.toEntity(member);

        personalDashboardController = new PersonalDashboardController(personalDashboardService);

        mockMvc = MockMvcBuilders.standaloneSetup(personalDashboardController)
                .apply(documentationConfiguration(restDocumentation))
                .setCustomArgumentResolvers(new CurrentUserEmailArgumentResolver(tokenProvider))
                .setControllerAdvice(new ControllerAdvice())
                .build();

        when(tokenProvider.getUserEmailFromToken(any(TokenReqDto.class))).thenReturn("email");
    }

    @DisplayName("POST 개인 대시보드 저장 컨트롤러 로직 확인")
    @Test
    void 개인_대시보드_저장() throws Exception {
        // given
        PersonalDashboardInfoResDto response = PersonalDashboardInfoResDto.from(personalDashboard);
        given(personalDashboardService.save(anyString(), any(PersonalDashboardSaveReqDto.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/dashboards/")
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personalDashboardSaveReqDto)))
                .andDo(print())
                .andDo(document("dashboard/save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("title").description("개인 대시보드 제목"),
                                fieldWithPath("description").description("개인 대시보드 설명"),
                                fieldWithPath("isPublic").description("개인 대시보드 공개 범위"),
                                fieldWithPath("category").description("개인 대시보드 카테고리")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.title").description("개인 대시보드 제목"),
                                fieldWithPath("data.description").description("개인 대시보드 설명"),
                                fieldWithPath("data.isPublic").description("개인 대시보드 공개 범위"),
                                fieldWithPath("data.category").description("개인 대시보드 카테고리")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("PATCH 개인 대시보드 수정 컨트롤러 로직 확인")
    @Test
    void 개인_대시보드_수정() throws Exception {
        // given
        personalDashboard.update(personalDashboardUpdateReqDto.title(),
                personalDashboardUpdateReqDto.description(),
                personalDashboardUpdateReqDto.isPublic(),
                personalDashboardUpdateReqDto.category());
        PersonalDashboardInfoResDto response = PersonalDashboardInfoResDto.from(personalDashboard);
        given(personalDashboardService.update(anyString(), anyLong(),
                any(PersonalDashboardUpdateReqDto.class))).willReturn(response);

        // when & then
        mockMvc.perform(patch("/api/dashboards/{dashboardId}", 1L)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personalDashboardUpdateReqDto)))
                .andDo(print())
                .andDo(document("dashboard/save",
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
                                fieldWithPath("description").description("개인 대시보드 설명"),
                                fieldWithPath("isPublic").description("개인 대시보드 공개 범위"),
                                fieldWithPath("category").description("개인 대시보드 카테고리")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.title").description("개인 대시보드 제목"),
                                fieldWithPath("data.description").description("개인 대시보드 설명"),
                                fieldWithPath("data.isPublic").description("개인 대시보드 공개 범위"),
                                fieldWithPath("data.category").description("개인 대시보드 카테고리")
                        )
                ))
                .andExpect(status().isOk());
    }
}