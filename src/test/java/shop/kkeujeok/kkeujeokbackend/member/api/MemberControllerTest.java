package shop.kkeujeok.kkeujeokbackend.member.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shop.kkeujeok.kkeujeokbackend.common.annotation.ControllerTest;
import shop.kkeujeok.kkeujeokbackend.global.annotationresolver.CurrentUserEmailArgumentResolver;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.TokenReqDto;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.MyPageInfoResDto;

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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.responseFields;

public class MemberControllerTest extends ControllerTest {

    @InjectMocks
    private MemberController memberController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        memberController = new MemberController(myPageService);

        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .apply(documentationConfiguration(restDocumentation))
                .setCustomArgumentResolvers(new CurrentUserEmailArgumentResolver(tokenProvider))
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
}
