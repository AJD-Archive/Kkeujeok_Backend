package shop.kkeujeok.kkeujeokbackend.auth.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보 DTO")
public record UserInfo(
        @Schema(description = "이메일", example = "user@example.com")
        String email,

        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "프로필 사진 URL", example = "http://example.com/profile.jpg")
        String picture,

        @Schema(description = "닉네임", example = "길동이")
        String nickname
) {
}
