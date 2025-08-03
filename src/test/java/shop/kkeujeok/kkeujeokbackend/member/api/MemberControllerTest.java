package shop.kkeujeok.kkeujeokbackend.member.api;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.responseFields;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.TokenReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeListResDto;
import shop.kkeujeok.kkeujeokbackend.common.annotation.ControllerTest;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardPageListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardListResDto;
import shop.kkeujeok.kkeujeokbackend.global.annotationresolver.CurrentUserEmailArgumentResolver;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.Role;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.MyPageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.PersonalDashboardsAndChallengesResDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.TeamDashboardsAndChallengesResDto;

public class MemberControllerTest extends ControllerTest {

    @InjectMocks
    private MemberController memberController;

    private Member member;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        memberController = new MemberController(myPageService);

        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .apply(documentationConfiguration(restDocumentation))
                .setCustomArgumentResolvers(new CurrentUserEmailArgumentResolver(tokenProvider),
                        new PageableHandlerMethodArgumentResolver()) // 추가
                .build();

        member = Member.builder()
                .status(Status.ACTIVE)
                .email("kkeujeok@gmail.com")
                .name("김동균")
                .picture("기본 프로필")
                .socialType(SocialType.GOOGLE)
                .role(Role.ROLE_USER)
                .firstLogin(true)
                .nickname("동동")
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
                "introduction",
                1L,
                "#1234");

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
                                fieldWithPath("data.introduction").description("회원 소개"),
                                fieldWithPath("data.memberId").description("회원 ID"),
                                fieldWithPath("data.tag").description("고유 번호 ex) #1234")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("내 프로필 정보")))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("대시보드와 챌린지 정보를 가져옵니다.")
    @Test
    void 팀_대시보드와_챌린지_정보를_가져옵니다() throws Exception {
        // Given
        TeamDashboardsAndChallengesResDto resDto = new TeamDashboardsAndChallengesResDto(
                new PersonalDashboardPageListResDto(new ArrayList<>(), new PageInfoResDto(0, 0, 0)),
                new TeamDashboardListResDto(new ArrayList<>(), new PageInfoResDto(0, 0, 0)),
                new ChallengeListResDto(new ArrayList<>(), new PageInfoResDto(0, 0, 0))
        );

        when(myPageService.findTeamDashboardsAndChallenges(anyString(), anyString(), any(PageRequest.class)))
                .thenReturn(resDto);

        when(tokenProvider.getUserEmailFromToken(any(TokenReqDto.class))).thenReturn("test@example.com");

        mockMvc.perform(get("/api/members/mypage/dashboard-challenges")
                        .header("Authorization", "Bearer valid-token")
                        .param("requestEmail", "request@example.com")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andDo(document("member/team-challengeInfoResDto",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        queryParameters(
                                parameterWithName("requestEmail").description("조회할 사용자의 이메일"),
                                parameterWithName("page").description("페이지 번호 (기본값: 0)"),
                                parameterWithName("size").description("페이지 당 항목 수 (기본값: 10)")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.personalDashboardList.personalDashboardInfoResDto").description(
                                        "개인 대시보드 정보 목록"),
                                fieldWithPath("data.personalDashboardList.pageInfoResDto.currentPage").description(
                                        "현재 페이지 번호"),
                                fieldWithPath("data.personalDashboardList.pageInfoResDto.totalPages").description(
                                        "총 페이지 수"),
                                fieldWithPath("data.personalDashboardList.pageInfoResDto.totalItems").description(
                                        "총 항목 수"),
                                fieldWithPath("data.teamDashboardList.teamDashboardInfoResDto").description(
                                        "팀 대시보드 정보 목록"),
                                fieldWithPath("data.teamDashboardList.pageInfoResDto.currentPage").description(
                                        "현재 페이지 번호"),
                                fieldWithPath("data.teamDashboardList.pageInfoResDto.totalPages").description(
                                        "총 페이지 수"),
                                fieldWithPath("data.teamDashboardList.pageInfoResDto.totalItems").description("총 항목 수"),
                                fieldWithPath("data.challengeList.challengeInfoResDto").description("챌린지 정보 목록"),
                                fieldWithPath("data.challengeList.pageInfoResDto.currentPage").description("현재 페이지 번호"),
                                fieldWithPath("data.challengeList.pageInfoResDto.totalPages").description("총 페이지 수"),
                                fieldWithPath("data.challengeList.pageInfoResDto.totalItems").description("총 항목 수")

                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("대시보드와 챌린지 정보 조회")))
                .andExpect(jsonPath("$.data").exists());
    }

//    @DisplayName("PATCH 내 프로필 정보 수정 테스트")
//    @Test
//    void 내_프로필_정보_수정() throws Exception {
//        // given
//        MyPageUpdateReqDto updateReqDto = new MyPageUpdateReqDto("귀여운수달", "안녕하세요?");
//
//       member.update(updateReqDto.nickname(), updateReqDto.introduction());
//
//       MyPageInfoResDto myPageInfoResDto = MyPageInfoResDto.From(member);
//
//        given(myPageService.update(anyString(), any(MyPageUpdateReqDto.class)))
//                .willReturn(myPageInfoResDto);
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

    @DisplayName("친구의 프로필 정보를 가져옵니다.")
    @Test
    void 친구_프로필_정보를_가져옵니다() throws Exception {
        // Given
        Long friendId = 1L;
        MyPageInfoResDto friendProfileDto = new MyPageInfoResDto(
                "friendPicture",
                "friend@example.com",
                "친구이름",
                "친구닉네임",
                SocialType.GOOGLE,
                "친구소개",
                2L,
                "#1234"
        );

        when(myPageService.findFriendProfile(friendId)).thenReturn(friendProfileDto);

        // When & Then
        mockMvc.perform(get("/api/members/mypage/{memberId}", friendId)
                        .header("Authorization", "Bearer valid-token"))
                .andDo(print())
                .andDo(document("member/friend-profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.picture").description("친구 사진"),
                                fieldWithPath("data.email").description("친구 이메일"),
                                fieldWithPath("data.name").description("친구 이름"),
                                fieldWithPath("data.nickName").description("친구 닉네임"),
                                fieldWithPath("data.socialType").description("친구 소셜 타입"),
                                fieldWithPath("data.introduction").description("친구 소개"),
                                fieldWithPath("data.memberId").description("친구 ID"),
                                fieldWithPath("data.tag").description("고유 번호 ex) #1234")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("친구 프로필 정보 조회")))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.email", is("friend@example.com")))
                .andExpect(jsonPath("$.data.name", is("친구이름")))
                .andExpect(jsonPath("$.data.nickName", is("친구닉네임")))
                .andExpect(jsonPath("$.data.socialType", is(SocialType.GOOGLE.name())))
                .andExpect(jsonPath("$.data.introduction", is("친구소개")));
    }

    @DisplayName("친구의 public 개인 대시보드와 챌린지 정보를 가져옵니다.")
    @Test
    void 친구_대시보드와_챌린지_정보를_가져옵니다() throws Exception {
        // Given
        PersonalDashboardPageListResDto personalDashboardList = new PersonalDashboardPageListResDto(
                new ArrayList<>(),
                new PageInfoResDto(0, 0, 0)
        );

        ChallengeListResDto challengeList = new ChallengeListResDto(
                new ArrayList<>(),
                new PageInfoResDto(0, 0, 0)
        );

        PersonalDashboardsAndChallengesResDto resDto = new PersonalDashboardsAndChallengesResDto(
                personalDashboardList,
                challengeList
        );

        Long friendId = 1L;

        when(myPageService.findFriendDashboardsAndChallenges(friendId, PageRequest.of(0, 10))).thenReturn(resDto);

        // When & Then
        mockMvc.perform(get("/api/members/mypage/{memberId}/dashboard-challenges", friendId)
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andDo(document("member/friend-dashboard-challenges",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (기본값: 0)"),
                                parameterWithName("size").description("페이지 당 항목 수 (기본값: 10)")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.personalDashboardList.personalDashboardInfoResDto").description(
                                        "개인 대시보드 정보 목록"),
                                fieldWithPath("data.personalDashboardList.pageInfoResDto.currentPage").description(
                                        "현재 페이지 번호"),
                                fieldWithPath("data.personalDashboardList.pageInfoResDto.totalPages").description(
                                        "총 페이지 수"),
                                fieldWithPath("data.personalDashboardList.pageInfoResDto.totalItems").description(
                                        "총 항목 수"),
                                fieldWithPath("data.challengeList.challengeInfoResDto").description("챌린지 정보 목록"),
                                fieldWithPath("data.challengeList.pageInfoResDto.currentPage").description("현재 페이지 번호"),
                                fieldWithPath("data.challengeList.pageInfoResDto.totalPages").description("총 페이지 수"),
                                fieldWithPath("data.challengeList.pageInfoResDto.totalItems").description("총 항목 수")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.message", is("대시보드와 챌린지 정보 조회")))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.personalDashboardList").exists())
                .andExpect(jsonPath("$.data.personalDashboardList.personalDashboardInfoResDto").isArray())
                .andExpect(jsonPath("$.data.personalDashboardList.pageInfoResDto.currentPage", is(0)))
                .andExpect(jsonPath("$.data.personalDashboardList.pageInfoResDto.totalPages", is(0)))
                .andExpect(jsonPath("$.data.personalDashboardList.pageInfoResDto.totalItems", is(0)))
                .andExpect(jsonPath("$.data.challengeList").exists())
                .andExpect(jsonPath("$.data.challengeList.challengeInfoResDto").isArray())
                .andExpect(jsonPath("$.data.challengeList.pageInfoResDto.currentPage", is(0)))
                .andExpect(jsonPath("$.data.challengeList.pageInfoResDto.totalPages", is(0)))
                .andExpect(jsonPath("$.data.challengeList.pageInfoResDto.totalItems", is(0)));
    }

}
