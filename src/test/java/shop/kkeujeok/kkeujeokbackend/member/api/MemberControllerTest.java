package shop.kkeujeok.kkeujeokbackend.member.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shop.kkeujeok.kkeujeokbackend.common.annotation.ControllerTest;
import shop.kkeujeok.kkeujeokbackend.global.annotationresolver.CurrentUserEmailArgumentResolver;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.TokenReqDto;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.MyPageInfoResDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

public class MemberControllerTest extends ControllerTest {

    @InjectMocks
    private MemberController memberController;

    @BeforeEach
    void setUp() {
        memberController = new MemberController(myPageService, nicknameService);

        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
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

        mockMvc.perform(get("/api/members/mypage").header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("내 프로필 정보")))
                .andExpect(jsonPath("$.data").exists());
    }
}
