package shop.kkeujeok.kkeujeokbackend.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.RefreshTokenReqDto;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.response.MemberLoginResDto;
import shop.kkeujeok.kkeujeokbackend.global.jwt.TokenProvider;
import shop.kkeujeok.kkeujeokbackend.global.jwt.api.dto.TokenDto;
import shop.kkeujeok.kkeujeokbackend.global.jwt.domain.Token;
import shop.kkeujeok.kkeujeokbackend.global.jwt.domain.repository.TokenRepository;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
public class TokenService {

    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    public TokenService(TokenProvider tokenProvider, TokenRepository tokenRepository, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.tokenRepository = tokenRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public TokenDto getToken(MemberLoginResDto memberLoginResDto) {
        TokenDto tokenDto = tokenProvider.generateToken(memberLoginResDto.findMember().getEmail());

        tokenSaveAndUpdate(memberLoginResDto, tokenDto);

        return tokenDto;
    }

    private void tokenSaveAndUpdate(MemberLoginResDto memberLoginResDto, TokenDto tokenDto) {
        if (!tokenRepository.existsByMember(memberLoginResDto.findMember())) {
            tokenRepository.save(Token.builder()
                    .member(memberLoginResDto.findMember())
                    .refreshToken(tokenDto.refreshToken())
                    .build());
        }

        refreshTokenUpdate(memberLoginResDto, tokenDto);
    }

    private void refreshTokenUpdate(MemberLoginResDto memberLoginResDto, TokenDto tokenDto) {
        Token token = tokenRepository.findByMember(memberLoginResDto.findMember()).orElseThrow();
        token.refreshTokenUpdate(tokenDto.refreshToken());
    }

    @Transactional
    public TokenDto generateAccessToken(RefreshTokenReqDto refreshTokenReqDto) {
        if (!tokenRepository.existsByRefreshToken(refreshTokenReqDto.refreshToken()) || !tokenProvider.validateToken(refreshTokenReqDto.refreshToken())) {
            throw new RuntimeException();
        }

        Token token = tokenRepository.findByRefreshToken(refreshTokenReqDto.refreshToken()).orElseThrow();
        Member member = memberRepository.findById(token.getMember().getMemberId()).orElseThrow();

        return tokenProvider.generateAccessTokenByRefreshToken(member.getEmail(), token.getRefreshToken());
    }

}
