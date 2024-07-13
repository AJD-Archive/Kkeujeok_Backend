package shop.kkeujeok.kkeujeokbackend.auth.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceFactoryTest {

    @Mock
    private AuthService authService1;

    @Mock
    private AuthService authService2;

    private AuthServiceFactory authServiceFactory;

    @BeforeEach
    void setUp() {
        when(authService1.getProvider()).thenReturn("provider1");
        when(authService2.getProvider()).thenReturn("provider2");

        List<AuthService> authServiceList = Arrays.asList(authService1, authService2);
        authServiceFactory = new AuthServiceFactory(authServiceList);
    }

    @DisplayName("특정 provider에 맞는 AuthService를 반환한다")
    @Test
    void 특정_provider에_맞는_AuthService를_반환한다() {
        AuthService result = authServiceFactory.getAuthService("provider1");
        assertThat(result).isEqualTo(authService1);

        result = authServiceFactory.getAuthService("provider2");
        assertThat(result).isEqualTo(authService2);
    }
}
