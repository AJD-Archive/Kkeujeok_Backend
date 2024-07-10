package shop.kkeujeok.kkeujeokbackend.global.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.kkeujeok.kkeujeokbackend.global.jwt.api.dto.TokenDto;

import java.security.Key;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenProviderTest {

    private final String accessTokenExpireTime = "3600";
    private final String refreshTokenExpireTime = "3600";
    private final String secret = "A".repeat(128);

    private TokenProvider tokenProvider;

    @BeforeEach
    void init() {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        tokenProvider = new TokenProvider(accessTokenExpireTime, "3600", secret, key);
        tokenProvider.init();
    }

    @DisplayName("엑세스 토큰을 생성한다.")
    @Test
    void 엑세스_토큰을_생성한다() {
        // given
        String email = "inho@gmail.com";

        // when
        String actual = tokenProvider.generateAccessToken(email);

        // then
        // 배열의 크기가 3인지 확인하는 테스트 코드
        assertThat(actual.split("\\.")).hasSize(3);
    }

    @DisplayName("리프레시 토큰을 생성한다.")
    @Test
    void 리프레시_토큰을_생성한다() {

        // given, when
        String actual = tokenProvider.generateRefreshToken();

        // then
        // 배열의 크기가 3인지 확인하는 테스트 코드
        assertThat(actual.split("\\.")).hasSize(3);
    }

    @DisplayName("토큰들을 반환한다.")
    @Test
    void 토큰들을_반환한다() {
        // given
        String email = "inho@gmail.com";

        // when
        TokenDto actual = tokenProvider.generateToken(email);

        //then
        assertThat(actual).isNotNull(); // 토큰이 null이 아닌지 확인
    }

    @DisplayName("리프레시 토큰으로 엑세스 토큰을 반환한다.")
    @Test
    void 리프레시_토큰으로_엑세스_토큰을_반환한다() {
        // given
        String refreshToken = "refreshToken";
        String email = "inho@gmail.com";

        //when
        TokenDto actual = tokenProvider.generateAccessTokenByRefreshToken(email, refreshToken);

        //then
        assertThat(actual).isNotNull();
    }
}
