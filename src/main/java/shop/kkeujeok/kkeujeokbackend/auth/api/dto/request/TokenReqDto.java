package shop.kkeujeok.kkeujeokbackend.auth.api.dto.request;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 발급 요청 DTO")
public record TokenReqDto(
        @Schema(description = "인증 코드", example = "auth_code_example")
        @NotNull(message = "인증 코드는 필수입니다.")
        String authCode
) {
}
