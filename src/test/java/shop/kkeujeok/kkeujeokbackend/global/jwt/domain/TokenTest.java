package shop.kkeujeok.kkeujeokbackend.global.jwt.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenTest {
    @DisplayName("refresh token을 교체한다.")
    @Test
    void refresh_token을_교체한다() {
        // given
        Member 인호 = new Member();
        String refreshToken = "adasaegsfadasdasfgfgrgredksgdffa";
        Token oAuthToken = new Token(인호, refreshToken);

        String updatedRefreshToken = "dfgsbnskjglnafgkajfnakfjgngejlkrqgn";

        // when
        oAuthToken.refreshTokenUpdate(updatedRefreshToken);

        // then
        assertThat(oAuthToken.getRefreshToken()).isEqualTo(updatedRefreshToken);
    }


}
