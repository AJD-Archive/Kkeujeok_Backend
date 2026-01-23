package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.Follow;

@Builder
@Schema(description = "친구 추가 수락 응답 DTO")
public record FollowAcceptResDto(
        @Schema(description = "요청한 회원 ID", example = "1")
        @NotNull(message = "요청한 회원 ID는 필수입니다.")
        Long fromMemberId,

        @Schema(description = "수락한 회원 ID", example = "2")
        @NotNull(message = "수락한 회원 ID는 필수입니다.")
        Long toMemberId
) {
    public static FollowAcceptResDto from(Follow follow) {
        return FollowAcceptResDto.builder()
                .fromMemberId(follow.getFromMember().getId())
                .toMemberId(follow.getToMember().getId())
                .build();
    }
}
