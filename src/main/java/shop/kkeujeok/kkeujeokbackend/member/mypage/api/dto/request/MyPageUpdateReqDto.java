package shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.request;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "마이페이지 수정 요청 DTO")
public record MyPageUpdateReqDto(
        @Schema(description = "닉네임", example = "홍길동")
        @NotNull(message = "닉네임은 필수입니다.")
        String nickname,

        @Schema(description = "자기소개", example = "안녕하세요. 개발자 홍길동입니다.")
        @NotNull(message = "자기소개는 필수입니다.")
        String introduction
) {
}
