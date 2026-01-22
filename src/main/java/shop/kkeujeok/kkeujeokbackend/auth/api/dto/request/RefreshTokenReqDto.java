package shop.kkeujeok.kkeujeokbackend.auth.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리프레시 토큰 요청 DTO")
public record RefreshTokenReqDto(
        @Schema(description = "리프레시 토큰", example = "refresh_token_example")
        String refreshToken
){
}
