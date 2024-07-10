package shop.kkeujeok.kkeujeokbackend.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import shop.kkeujeok.kkeujeokbackend.global.jwt.api.dto.TokenDto;

import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {

    @Value("${token.expire.time.access}")
    private String accessTokenExpireTime;

    @Value("${token.expire.time.refresh}")
    private String refreshTokenExpireTime;

    @Value("${jwt.secret}")
    private String secret;
    private Key key;

    public TokenProvider(String accessTokenExpireTime, String refreshTokenExpireTime, String secret, Key key) {
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
        this.secret = secret;
        this.key = key;
    }

    @PostConstruct
    public void init() {
        byte[] key = Decoders.BASE64URL.decode(secret);
        this.key = Keys.hmacShaKeyFor(key);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (UnsupportedJwtException | MalformedJwtException exception) {
            log.error("JWT is not valid");
        } catch (SignatureException exception) {
            log.error("JWT signature validation fails");
        } catch (ExpiredJwtException exception) {
            log.error("JWT expired");
        } catch (IllegalArgumentException exception) {
            log.error("JWT is null or empty or only whitespace");
        } catch (Exception exception) {
            log.error("JWT validation fails", exception);
        }

        return false;
    }

    public TokenDto generateToken(String email) {
        String accessToken = generateAccessToken(email);
        String refreshToken = generateRefreshToken();

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenDto generateAccessTokenByRefreshToken(String email, String refreshToken) {
        String accessToken = generateAccessToken(email);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String generateAccessToken(String email) {
        Date date = new Date();
        Date accessExpiryDate = new Date(date.getTime() + Long.parseLong(accessTokenExpireTime));

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(date)
                .setExpiration(accessExpiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken() {
        Date date = new Date();
        Date refreshExpiryDate = new Date(date.getTime() + Long.parseLong(refreshTokenExpireTime));

        return Jwts.builder()
                .setExpiration(refreshExpiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

// 시큐리티 규현 고민 중..
//    public Authentication getAuthentication(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        Member member = memberRepository.findByEmail(claims.getSubject()).orElseThrow();
//
//        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(member.getRole().toString()));
//
//        return new UsernamePasswordAuthenticationToken(member, "", authorities);
//    }
}
