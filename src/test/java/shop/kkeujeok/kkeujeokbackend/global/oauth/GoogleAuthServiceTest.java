package shop.kkeujeok.kkeujeokbackend.global.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.response.UserInfo;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class GoogleAuthServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GoogleAuthService googleAuthService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        googleAuthService = new GoogleAuthService(objectMapper);
    }

    // getIdToken 테스트코드..

    @DisplayName("올바르게 구글 소셜 정보가 넘어가는지 확인합니다.")
    @Test
    void 올바르게_구글_소셜_정보가_넘어가는지_확인합니다() {
        String provider = "google";
        String actualProvider = googleAuthService.getProvider();

        assertEquals(provider, actualProvider);
    }

    @DisplayName("JWT 토큰을 사용하여 유저 정보를 반환합니다.")
    @Test
    void JWT_토큰을_사용하여_유저_정보를_반환합니다() throws Exception {
        String token = "test.test.test";

        String decodePayload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);
        UserInfo userInfo = new UserInfo("email", "name", "picture","nickname");

        when(objectMapper.readValue(decodePayload, UserInfo.class)).thenReturn(userInfo);

        assertNotNull(googleAuthService.getUserInfo(token));
    }
}
