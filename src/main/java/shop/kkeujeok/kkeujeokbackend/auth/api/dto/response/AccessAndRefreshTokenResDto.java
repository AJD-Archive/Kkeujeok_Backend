package shop.kkeujeok.kkeujeokbackend.auth.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "액세스 및 리프레시 토큰 응답 DTO")
public record AccessAndRefreshTokenResDto(
        @Schema(description = "액세스 토큰", example = "access_token_example")
        String accessToken,

        @Schema(description = "리프레시 토큰", example = "refresh_token_example")
        String refreshToken
) {
}
