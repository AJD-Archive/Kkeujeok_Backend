package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.request;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "친구 추가 수락 요청 DTO")
public record FollowAcceptReqDto(
        @Schema(description = "팔로우 ID", example = "1")
        @NotNull(message = "팔로우 ID는 필수입니다.")
        Long followId
) {
}
