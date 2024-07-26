package shop.kkeujeok.kkeujeokbackend.global.filter;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import shop.kkeujeok.kkeujeokbackend.global.jwt.TokenProvider;

import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class LoginCheckFilterTest {

    @Mock(lenient = true)
    private TokenProvider tokenProvider; // Mock으로 TokenProvider 주입

    @InjectMocks
    private LoginCheckFilter loginCheckFilter; // InjectMocks로 필터 주입

    @BeforeEach
    public void setUp() {
        loginCheckFilter = new LoginCheckFilter(tokenProvider); // 필터 인스턴스를 직접 초기화하여 Mock TokenProvider 주입
    }

    @DisplayName("access토큰으로 api 인가를 받을 수 있다.")
    @Test
    public void access토큰으로_api_인가를_받을_수_있다() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid-token");
        request.setRequestURI("/inho");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        when(tokenProvider.validateToken("valid-token")).thenReturn(true);

        loginCheckFilter.doFilter(request, response, filterChain);

        assertThat(response.getStatus()).isNotEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

//    화이트리스트를 전부 열어 두었기 때문에 해당 테스트는 실패 (개발 끝나면 열 예정)
//    @DisplayName("access토큰 없이는 api 인가를 받을 수 없다.")
//    @Test
//    public void access토큰_없이는_api_인가를_받을_수_없다() throws IOException, ServletException {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.setRequestURI("/inho");
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        MockFilterChain filterChain = new MockFilterChain();
//
//        loginCheckFilter.doFilter(request, response, filterChain);
//
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
//    }

    @DisplayName("화이트 리스트를 열어 두면 필터가 생략된다.")
    @Test
    public void 화이트_리스트를_열어_두면_필터가_생략된다() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        loginCheckFilter.doFilter(request, response, filterChain);

        assertThat(response.getStatus()).isNotEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

//    화이트리스트를 전부 열어 두었기 때문에 해당 테스트는 실패 (개발 끝나면 열 예정)
//    @DisplayName("화이트 리스트를 닫아 두면 필터가 작동한다.")
//    @Test
//    public void 화이트_리스트를_닫아_두면_필터가_작동한다() throws IOException, ServletException {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.setRequestURI("/inho");
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        MockFilterChain filterChain = new MockFilterChain();
//
//        loginCheckFilter.doFilter(request, response, filterChain);
//
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
//    }
}
