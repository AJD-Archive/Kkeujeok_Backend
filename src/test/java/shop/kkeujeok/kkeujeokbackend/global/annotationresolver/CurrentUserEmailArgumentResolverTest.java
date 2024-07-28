package shop.kkeujeok.kkeujeokbackend.global.annotationresolver;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.TokenReqDto;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.jwt.TokenProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CurrentUserEmailArgumentResolverTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private NativeWebRequest webRequest;

    @Mock
    private MethodParameter methodParameter;

    @InjectMocks
    private CurrentUserEmailArgumentResolver resolver;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @DisplayName("어노테이션이 있을 때는 true를 반환하고, 없을 때는 false를 반환합니다.")
    @Test
    public void  어노테이션이_있을_때는_true를_반환하고_없을_때는_false를_반환합니다() {
        when(methodParameter.getParameterAnnotation(CurrentUserEmail.class)).thenReturn(mock(CurrentUserEmail.class));
        assertTrue(resolver.supportsParameter(methodParameter));

        when(methodParameter.getParameterAnnotation(CurrentUserEmail.class)).thenReturn(null);
        assertFalse(resolver.supportsParameter(methodParameter));
    }

    @DisplayName("토큰을 통해 이메일을 추출합니다.")
    @Test
    public void 토큰을_통해_이메일을_추출합니다() {
        String token = "Bearer someValidToken";
        String userEmail = "user@example.com";

        when(webRequest.getNativeRequest()).thenReturn(request);
        when(request.getHeader("Authorization")).thenReturn(token);
        when(tokenProvider.getUserEmailFromToken(any(TokenReqDto.class))).thenReturn(userEmail);

        Object result = resolver.resolveArgument(methodParameter,
                mock(ModelAndViewContainer.class),
                webRequest,
                mock(WebDataBinderFactory.class));

        assertEquals(userEmail, result);
    }

    @DisplayName("토큰이 없을 때 NULL을 호출합니다.")
    @Test
    public void 토큰이_없을_때_NULL을_호출합니다() {
        when(webRequest.getNativeRequest()).thenReturn(request);
        when(request.getHeader("Authorization")).thenReturn(null);

        Object result = resolver.resolveArgument(methodParameter,
                mock(ModelAndViewContainer.class),
                webRequest,
                mock(WebDataBinderFactory.class));

        assertNull(result);
    }

    @DisplayName("토큰이 잘못됐을 때 NULL을 호출합니다.")
    @Test
    public void 토큰이_잘못됐을_때_NULL을_호출합니다() {
        String token = "InvalidTokenFormat";

        when(webRequest.getNativeRequest()).thenReturn(request);
        when(request.getHeader("Authorization")).thenReturn(token);

        Object result = resolver.resolveArgument(methodParameter,
                mock(ModelAndViewContainer.class),
                webRequest,
                mock(WebDataBinderFactory.class));

        assertNull(result);
    }
}
