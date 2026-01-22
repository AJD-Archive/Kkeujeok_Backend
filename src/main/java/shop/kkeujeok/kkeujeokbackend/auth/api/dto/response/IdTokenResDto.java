package shop.kkeujeok.kkeujeokbackend.auth.api.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ID 토큰 응답 DTO")
public record IdTokenResDto(
        @Schema(description = "ID 토큰", example = "{\"alg\":\"RS256\",\"kid\":\"...\"}")
        JsonNode idToken
) {
}
