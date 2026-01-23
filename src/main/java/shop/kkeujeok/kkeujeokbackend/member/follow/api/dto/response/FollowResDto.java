package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.Follow;

@Builder
@Schema(description = "친구 추가 응답 DTO")
public record FollowResDto(
        @Schema(description = "친구 추가된 회원 ID", example = "2")
        @NotNull(message = "친구 추가된 회원 ID는 필수입니다.")
        Long toMemberId
) {
    public static FollowResDto from(Member member) {
        return FollowResDto.builder()
                .toMemberId(member.getId())
                .build();
    }
}
