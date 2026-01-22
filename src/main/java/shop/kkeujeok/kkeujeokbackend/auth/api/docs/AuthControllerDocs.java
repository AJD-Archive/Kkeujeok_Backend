package shop.kkeujeok.kkeujeokbackend.auth.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.RefreshTokenReqDto;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.TokenReqDto;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.response.IdTokenResDto;
import shop.kkeujeok.kkeujeokbackend.global.jwt.api.dto.TokenDto;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

@Tag(name = "AuthController", description = "인증 관련 API")
public interface AuthControllerDocs {

    @Operation(summary = "OAuth2 콜백", description = "OAuth2 인증 후 콜백을 처리하여 ID 토큰을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID 토큰 발급 성공")
    })
    IdTokenResDto callback(@PathVariable(name = "provider") String provider,
                           @RequestParam(name = "code") String code);

    @Operation(summary = "액세스 및 리프레시 토큰 발급", description = "소셜 로그인 인증 코드를 사용하여 액세스 및 리프레시 토큰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 발급 성공")
    })
    RspTemplate<TokenDto> generateAccessAndRefreshToken(@PathVariable(name = "provider") String provider,
                                                        @RequestBody TokenReqDto tokenReqDto);

    @Operation(summary = "액세스 토큰 재발급", description = "리프레시 토큰을 사용하여 액세스 토큰을 재발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "액세스 토큰 재발급 성공")
    })
    RspTemplate<TokenDto> generateAccessToken(@RequestBody RefreshTokenReqDto refreshTokenReqDto);
}
