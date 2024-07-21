package shop.kkeujeok.kkeujeokbackend.auth.api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.RefreshTokenReqDto;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.TokenReqDto;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.response.MemberLoginResDto;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.response.UserInfo;
import shop.kkeujeok.kkeujeokbackend.auth.application.AuthMemberService;
import shop.kkeujeok.kkeujeokbackend.auth.application.AuthService;
import shop.kkeujeok.kkeujeokbackend.auth.application.AuthServiceFactory;
import shop.kkeujeok.kkeujeokbackend.auth.application.TokenService;
import shop.kkeujeok.kkeujeokbackend.global.jwt.api.dto.TokenDto;
import shop.kkeujeok.kkeujeokbackend.global.oauth.GoogleAuthService;
import shop.kkeujeok.kkeujeokbackend.global.oauth.KakaoAuthService;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;

@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceFactory authServiceFactory;
    private final AuthMemberService memberService;
    private final TokenService tokenService;

    @GetMapping("oauth2/callback/google")
    public JsonNode googleCallback(@RequestParam(name = "code") String code) {
        AuthService googleAuthService = authServiceFactory.getAuthService("google");
        return googleAuthService.getIdToken(code);
    }

    @GetMapping("oauth2/callback/kakao")
    public JsonNode kakaoCallback(@RequestParam(name = "code") String code) {
        AuthService kakaoAuthService = authServiceFactory.getAuthService("kakao");
        return kakaoAuthService.getIdToken(code);
    }

//    @Operation(summary = "로그인 후 토큰 발급", description = "액세스, 리프레쉬 토큰을 발급합니다.")
    @PostMapping("/{provider}/token")
    public RspTemplate<TokenDto> generateAccessAndRefreshToken(
            @PathVariable(name = "provider") String provider,
            @RequestBody TokenReqDto tokenReqDto) {
        AuthService authService = authServiceFactory.getAuthService(provider);
        UserInfo userInfo = authService.getUserInfo(tokenReqDto.authCode());

        MemberLoginResDto getMemberDto = memberService.saveUserInfo(userInfo,
                SocialType.valueOf(provider.toUpperCase()));
        TokenDto getToken = tokenService.getToken(getMemberDto);

        return new RspTemplate<>(HttpStatus.OK, "토큰 발급", getToken);
    }

//    @Operation(summary = "액세스 토큰 재발급", description = "리프레쉬 토큰으로 액세스 토큰을 발급합니다.")
    @PostMapping("/token/access")
    public RspTemplate<TokenDto> generateAccessToken(@RequestBody RefreshTokenReqDto refreshTokenReqDto) {
        TokenDto getToken = tokenService.generateAccessToken(refreshTokenReqDto);

        return new RspTemplate<>(HttpStatus.OK, "액세스 토큰 발급", getToken);
    }

}
