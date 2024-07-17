package shop.kkeujeok.kkeujeokbackend.auth.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.RefreshTokenReqDto;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.response.MemberLoginResDto;
import shop.kkeujeok.kkeujeokbackend.global.jwt.TokenProvider;
import shop.kkeujeok.kkeujeokbackend.global.jwt.api.dto.TokenDto;
import shop.kkeujeok.kkeujeokbackend.global.jwt.domain.Token;
import shop.kkeujeok.kkeujeokbackend.global.jwt.domain.repository.TokenRepository;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private TokenService tokenService;

    private MemberLoginResDto memberLoginResDto;
    private TokenDto tokenDto;
    private Member member;
    private Token token;

    @BeforeEach
    void setUp() {
        member = Member.builder().email("test@example.com").build();

        memberLoginResDto = mock(MemberLoginResDto.class);
        tokenDto = mock(TokenDto.class);
        token = mock(Token.class);

        when(memberLoginResDto.findMember()).thenReturn(member);
        when(tokenProvider.generateToken(anyString())).thenReturn(tokenDto);
        when(tokenRepository.findByMember(any(Member.class))).thenReturn(Optional.of(token));
        when(tokenDto.refreshToken()).thenReturn("new-refresh-token");
    }

    @DisplayName("accessToken과 refreshToken을 생성합니다.")
    @Test
    void accessToken과_refreshToken을_생성합니다() {
        when(tokenRepository.existsByMember(any(Member.class))).thenReturn(false);

        TokenDto result = tokenService.getToken(memberLoginResDto);

        assertNotNull(result);
        verify(tokenProvider).generateToken(member.getEmail());
        verify(tokenRepository).existsByMember(member);
        verify(tokenRepository).save(any(Token.class));
        verify(token).refreshTokenUpdate("new-refresh-token");
    }

// 하다가 벽느낀 테스트.
//    @DisplayName("refreshToken으로 accessToken를 재생성한다.")
//    @Test
//    void refreshToken으로_accessToken를_재생성한다.() {
//        // given
//        String refreshToken = "refresh-token";
//        RefreshTokenReqDto refreshTokenReqDto = new RefreshTokenReqDto(refreshToken);
//        Token token = new Token(member, refreshToken);
//
//        when(tokenRepository.existsByRefreshToken(refreshToken)).thenReturn(true);
//        when(tokenProvider.validateToken(refreshToken)).thenReturn(true);
//        when(tokenRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.of(token));
//        // Here we mock the memberRepository.findById to handle null or any Long
//        when(memberRepository.findById(anyLong())).thenAnswer(invocation -> {
//            Long id = invocation.getArgument(0);
//            return id == null ? Optional.empty() : Optional.of(member);
//        });
//        when(tokenProvider.generateAccessTokenByRefreshToken(anyString(), anyString())).thenReturn(tokenDto);
//
//        // when
//        TokenDto result = tokenService.generateAccessToken(refreshTokenReqDto);
//
//        // then
//        assertNotNull(result);
//        verify(tokenRepository).existsByRefreshToken(refreshToken);
//        verify(tokenProvider).validateToken(refreshToken);
//        verify(tokenRepository).findByRefreshToken(refreshToken);
//        verify(memberRepository).findById(anyLong());
//        verify(tokenProvider).generateAccessTokenByRefreshToken(member.getEmail(), refreshToken);
//    }
}
