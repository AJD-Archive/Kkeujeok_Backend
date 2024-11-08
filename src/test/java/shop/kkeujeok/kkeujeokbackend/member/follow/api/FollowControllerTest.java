package shop.kkeujeok.kkeujeokbackend.member.follow.api;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.TokenReqDto;
import shop.kkeujeok.kkeujeokbackend.common.annotation.ControllerTest;
import shop.kkeujeok.kkeujeokbackend.global.annotationresolver.CurrentUserEmailArgumentResolver;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.global.error.ControllerAdvice;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.request.FollowReqDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.FollowResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.FollowInfoListDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.RecommendedFollowInfoListDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.FollowInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.RecommendedFollowInfoResDto;

import java.util.Collections;

class FollowControllerTest extends ControllerTest {

    @InjectMocks
    private FollowController followController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        MockitoAnnotations.openMocks(this);

        followController = new FollowController(followService);

        mockMvc = MockMvcBuilders.standaloneSetup(followController)
                .apply(documentationConfiguration(restDocumentation))
                .setCustomArgumentResolvers(new CurrentUserEmailArgumentResolver(tokenProvider))
                .setControllerAdvice(new ControllerAdvice())
                .build();

        when(tokenProvider.getUserEmailFromToken(any(TokenReqDto.class))).thenReturn("email");
    }

    @DisplayName("POST 친구 추가 요청")
    @Test
    void 친구_추가_요청() throws Exception {
        FollowReqDto request = new FollowReqDto(2L);
        FollowResDto response = new FollowResDto(2L);
        given(followService.save(anyString(), any(FollowReqDto.class))).willReturn(response);

        mockMvc.perform(post("/api/member/follow")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("follow/save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("memberId").description("친구 추가 대상의 멤버 ID")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.toMemberId").description("친구로 추가된 멤버 ID")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("POST 친구 요청 수락")
    @Test
    void 친구_요청_수락() throws Exception {
        doNothing().when(followService).accept(anyLong());

        mockMvc.perform(post("/api/member/follow/accept/{followId}", 1L)
                        .header("Authorization", "Bearer valid-token"))
                .andDo(print())
                .andDo(document("follow/accept",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        pathParameters(
                                parameterWithName("followId").description("친구 요청 ID")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").optional().description("응답 데이터. 친구 추가 수락의 경우 null 반환")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("GET 내 친구 목록 조회")
    @Test
    void 친구_목록_조회() throws Exception {
        FollowInfoListDto response = new FollowInfoListDto(
                Collections.singletonList(new FollowInfoResDto(1L, "nickname", "name", "profileImage")),
                new PageInfoResDto(0, 10, 1)
        );
        given(followService.findFollowList(anyString(), any())).willReturn(response);

        mockMvc.perform(get("/api/member/follow")
                        .header("Authorization", "Bearer valid-token")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andDo(document("follow/findFollowList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.followInfoResDto[].memberId").description("친구 멤버 ID"),
                                fieldWithPath("data.followInfoResDto[].nickname").description("친구 닉네임"),
                                fieldWithPath("data.followInfoResDto[].name").description("친구 이름"),
                                fieldWithPath("data.followInfoResDto[].profileImage").description("친구 프로필 이미지"),
                                fieldWithPath("data.pageInfoResDto.currentPage").description("현재 페이지 번호"),
                                fieldWithPath("data.pageInfoResDto.totalPages").description("전체 페이지 수"),
                                fieldWithPath("data.pageInfoResDto.totalItems").description("전체 항목 수")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("GET 추천 친구 목록 조회")
    @Test
    void 추천_친구_목록_조회() throws Exception {
        RecommendedFollowInfoListDto response = new RecommendedFollowInfoListDto(
                Collections.singletonList(new RecommendedFollowInfoResDto(2L, "nickname2", "name2", "profileImage2")),
                new PageInfoResDto(0, 10, 1)
        );
        given(followService.findRecommendedFollowList(anyString(), any())).willReturn(response);

        mockMvc.perform(get("/api/member/follow/recommended")
                        .header("Authorization", "Bearer valid-token")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andDo(document("follow/findRecommendedFollowList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.recommendedFollowInfoResDtos[].memberId").description(
                                        "추천 친구 멤버 ID"),
                                fieldWithPath("data.recommendedFollowInfoResDtos[].nickname").description("추천 친구 닉네임"),
                                fieldWithPath("data.recommendedFollowInfoResDtos[].name").description("추천 친구 이름"),
                                fieldWithPath("data.recommendedFollowInfoResDtos[].profileImage").description(
                                        "추천 친구 프로필 이미지"),
                                fieldWithPath("data.pageInfoResDto.currentPage").description("현재 페이지 번호"),
                                fieldWithPath("data.pageInfoResDto.totalPages").description("전체 페이지 수"),
                                fieldWithPath("data.pageInfoResDto.totalItems").description("전체 항목 수")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("DELETE 친구 삭제")
    @Test
    void 친구_삭제() throws Exception {
        doNothing().when(followService).delete(anyString(), anyLong());

        mockMvc.perform(delete("/api/member/follow/{memberId}", 1L)
                        .header("Authorization", "Bearer valid-token"))
                .andDo(print())
                .andDo(document("follow/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        pathParameters(
                                parameterWithName("memberId").description("친구 관계를 해제할 멤버 ID")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").optional().description("응답 데이터. 친구 추가 수락의 경우 null 반환")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("GET 키워드로 추천 친구 목록 조회")
    @Test
    void 키워드로_추천_친구_목록_조회() throws Exception {
        RecommendedFollowInfoListDto response = new RecommendedFollowInfoListDto(
                Collections.singletonList(new RecommendedFollowInfoResDto(3L, "nickname3", "name3", "profileImage3")),
                new PageInfoResDto(0, 10, 1)
        );
        given(followService.searchRecommendedFollowUsingKeywords(anyString(), anyString(), any())).willReturn(response);

        mockMvc.perform(get("/api/member/follow/search")
                        .header("Authorization", "Bearer valid-token")
                        .param("keyword", "test")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andDo(document("follow/searchRecommendedFollowUsingKeywords",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        queryParameters(
                                parameterWithName("keyword").description("검색 키워드"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.recommendedFollowInfoResDtos[].memberId").description(
                                        "추천 친구 멤버 ID"),
                                fieldWithPath("data.recommendedFollowInfoResDtos[].nickname").description("추천 친구 닉네임"),
                                fieldWithPath("data.recommendedFollowInfoResDtos[].name").description("추천 친구 이름"),
                                fieldWithPath("data.recommendedFollowInfoResDtos[].profileImage").description(
                                        "추천 친구 프로필 이미지"),
                                fieldWithPath("data.pageInfoResDto.currentPage").description("현재 페이지 번호"),
                                fieldWithPath("data.pageInfoResDto.totalPages").description("전체 페이지 수"),
                                fieldWithPath("data.pageInfoResDto.totalItems").description("전체 항목 수")
                        )
                ))
                .andExpect(status().isOk());
    }
}
