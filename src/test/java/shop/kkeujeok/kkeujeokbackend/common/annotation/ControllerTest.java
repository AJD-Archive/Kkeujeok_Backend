package shop.kkeujeok.kkeujeokbackend.common.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import shop.kkeujeok.kkeujeokbackend.auth.api.AuthController;
import shop.kkeujeok.kkeujeokbackend.auth.application.AuthMemberService;
import shop.kkeujeok.kkeujeokbackend.auth.application.AuthService;
import shop.kkeujeok.kkeujeokbackend.auth.application.AuthServiceFactory;
import shop.kkeujeok.kkeujeokbackend.auth.application.TokenService;
import shop.kkeujeok.kkeujeokbackend.block.api.BlockController;
import shop.kkeujeok.kkeujeokbackend.block.application.BlockService;
import shop.kkeujeok.kkeujeokbackend.challenge.api.ChallengeController;
import shop.kkeujeok.kkeujeokbackend.challenge.application.ChallengeService;
import shop.kkeujeok.kkeujeokbackend.global.jwt.TokenProvider;

@AutoConfigureRestDocs
@WebMvcTest({
        BlockController.class,
        AuthController.class,
        ChallengeController.class
})
@ActiveProfiles("test")
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected BlockService blockService;

    @MockBean
    protected TokenProvider tokenProvider;

    @MockBean
    protected AuthServiceFactory authServiceFactory;

    @MockBean
    protected AuthMemberService authMemberService;

    @MockBean
    protected TokenService tokenService;

    @Mock
    protected AuthService authService;

    @MockBean
    protected ChallengeService challengeService;

}
