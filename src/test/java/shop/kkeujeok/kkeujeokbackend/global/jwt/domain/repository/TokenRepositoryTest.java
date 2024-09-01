package shop.kkeujeok.kkeujeokbackend.global.jwt.domain.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.kkeujeok.kkeujeokbackend.global.jwt.domain.Token;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenRepositoryTest {

    @Mock
    private TokenRepository tokenRepository;

    private Member member;
    private Token token;

    @BeforeEach
    void setUp() {
        member = Member.builder().email("test@example.com").build();
        token = Token.builder().member(member).refreshToken("refresh-token").build();
    }

    @DisplayName("member로 token이 존재하는지 확인한다.")

    @Test
    void member로_token이_존재하는지_확인한다() {
        // given
        when(tokenRepository.existsByMember(any(Member.class))).thenReturn(true);

        // when
        boolean exists = tokenRepository.existsByMember(member);

        // then
        assertThat(exists).isTrue();
    }

    @DisplayName("member로 token을 찾는다.")

    @Test
    void member로_token을_찾는다() {
        // given
        when(tokenRepository.findByMember(any(Member.class))).thenReturn(Optional.of(token));

        // when
        Optional<Token> foundToken = tokenRepository.findByMember(member);

        // then
        assertThat(foundToken).isPresent();
        assertThat(foundToken.get().getMember()).isEqualTo(member);
    }

    @DisplayName("refreshToken으로 token이 존재하는지 확인한다.")

    @Test
    void refreshToken으로_token이_존재하는지_확인한다() {
        // given
        when(tokenRepository.existsByRefreshToken(anyString())).thenReturn(true);

        // when
        boolean exists = tokenRepository.existsByRefreshToken("refresh-token");

        // then
        assertThat(exists).isTrue();
    }

    @DisplayName("refreshToken으로 token을 찾는다.")

    @Test
    void refreshToken으로_token을_찾는다() {
        // given
        when(tokenRepository.findByRefreshToken(anyString())).thenReturn(Optional.of(token));

        // when
        Optional<Token> foundToken = tokenRepository.findByRefreshToken("refresh-token");

        // then
        assertThat(foundToken).isPresent();
        assertThat(foundToken.get().getRefreshToken()).isEqualTo("refresh-token");
    }
}
