package shop.kkeujeok.kkeujeokbackend.member.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeListResDto;
import shop.kkeujeok.kkeujeokbackend.common.annotation.ControllerTest;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardListResDto;
import shop.kkeujeok.kkeujeokbackend.global.annotationresolver.CurrentUserEmailArgumentResolver;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.TokenReqDto;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.request.MyPageUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.MyPageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.TeamDashboardsAndChallengesResDto;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.requestFields;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.responseFields;

public class MemberControllerTest extends ControllerTest {

    @InjectMocks
    private MemberController memberController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        memberController = new MemberController(myPageService);

        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .apply(documentationConfiguration(restDocumentation))
                .setCustomArgumentResolvers(new CurrentUserEmailArgumentResolver(tokenProvider),
                        new PageableHandlerMethodArgumentResolver()) // 추가
                .build();
    }

    @DisplayName("내 프로필 정보를 가져옵니다.")
    @Test
    void 내_프로필_정보를_가져옵니다() throws Exception {
        MyPageInfoResDto myPageInfoResDto = new MyPageInfoResDto(
                "picture",
                "email",
                "name",
                "nickname",
                SocialType.GOOGLE,
                "introduction");

        when(myPageService.findMyProfileByEmail(anyString())).thenReturn(myPageInfoResDto);

        when(tokenProvider.getUserEmailFromToken(any(TokenReqDto.class))).thenReturn("email");

        mockMvc.perform(get("/api/members/mypage")
                        .header("Authorization", "Bearer valid-token"))
                .andDo(print())
                .andDo(document("member/mypage",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.picture").description("회원 사진"),
                                fieldWithPath("data.email").description("회원 이메일"),
                                fieldWithPath("data.name").description("회원 이름"),
                                fieldWithPath("data.nickName").description("회원 닉네임"),
                                fieldWithPath("data.socialType").description("회원 소셜 타입"),
                                fieldWithPath("data.introduction").description("회원 소개")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("내 프로필 정보")))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("팀 대시보드와 챌린지 정보를 가져옵니다.")
    @Test
    void 팀_대시보드와_챌린지_정보를_가져옵니다() throws Exception {
        TeamDashboardsAndChallengesResDto resDto = new TeamDashboardsAndChallengesResDto(
                new TeamDashboardListResDto(new ArrayList<>(), new PageInfoResDto(0, 0, 0)),
                new ChallengeListResDto(new ArrayList<>(), new PageInfoResDto(0, 0, 0))
        );

        when(myPageService.findTeamDashboardsAndChallenges(anyString(), any())).thenReturn(resDto);
        when(tokenProvider.getUserEmailFromToken(any(TokenReqDto.class))).thenReturn("email");

        mockMvc.perform(get("/api/members/mypage/dashboard-challenges")
                        .header("Authorization", "Bearer valid-token"))
                .andDo(print())
                .andDo(document("member/team-challenges",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.teamDashboardList.teamDashboardInfoResDto").description("팀 대시보드 정보 목록"),
                                fieldWithPath("data.teamDashboardList.pageInfoResDto.currentPage").description("현재 페이지 번호"),
                                fieldWithPath("data.teamDashboardList.pageInfoResDto.totalPages").description("총 페이지 수"),
                                fieldWithPath("data.teamDashboardList.pageInfoResDto.totalItems").description("총 항목 수"),
                                fieldWithPath("data.challengeList.challengeInfoResDto").description("챌린지 정보 목록"),
                                fieldWithPath("data.challengeList.pageInfoResDto.currentPage").description("현재 페이지 번호"),
                                fieldWithPath("data.challengeList.pageInfoResDto.totalPages").description("총 페이지 수"),
                                fieldWithPath("data.challengeList.pageInfoResDto.totalItems").description("총 항목 수")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("팀 대시보드와 챌린지 정보 조회")))
                .andExpect(jsonPath("$.data").exists());
    }

//    @DisplayName("PATCH 내 프로필 정보 수정 테스트")
//    @Test
//    void 내_프로필_정보_수정() throws Exception {
//        // given
//        MyPageUpdateReqDto updateReqDto = new MyPageUpdateReqDto("귀여운수달", "안녕하세요?");
//        MyPageInfoResDto updatedResDto = new MyPageInfoResDto(
//                "picture",
//                "chldlsgh0987@naver.com",
//                "최인호",
//                "귀여운수달",
//                SocialType.KAKAO,
//                "안녕하세요?"
//        );
//
//        // mock the service behavior
//        when(myPageService.update(anyString(), any(MyPageUpdateReqDto.class)))
//                .thenReturn(updatedResDto);
//
//        // when & then
//        mockMvc.perform(patch("/api/members/mypage")
//                        .header("Authorization", "Bearer valid-token")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateReqDto)))
//                .andDo(print())
//                .andDo(document("member/mypage-update",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestHeaders(
//                                headerWithName("Authorization").description("JWT 토큰")
//                        ),
//                        requestFields(
//                                fieldWithPath("nickname").description("변경할 닉네임"),
//                                fieldWithPath("introduction").description("변경할 소개")
//                        ),
//                        responseFields(
//                                fieldWithPath("statusCode").description("상태 코드"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("data.picture").description("회원 사진"),
//                                fieldWithPath("data.email").description("회원 이메일"),
//                                fieldWithPath("data.name").description("회원 이름"),
//                                fieldWithPath("data.nickName").description("회원 닉네임"),
//                                fieldWithPath("data.socialType").description("회원 소셜 타입"),
//                                fieldWithPath("data.introduction").description("회원 소개")
//                        )
//                ))
//                .andExpect(status().isOk());
//    }
}
