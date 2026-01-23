package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.request;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.Follow;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.FollowStatus;

@Schema(description = "친구 추가 요청 DTO")
public record FollowReqDto(
        @Schema(description = "친구 추가할 회원 ID", example = "2")
        @NotNull(message = "친구 추가할 회원 ID는 필수입니다.")
        Long memberId
) {
    public Follow toEntity(Member fromMember, Member toMember) {
        return Follow.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .followStatus(FollowStatus.WAIT)
                .build();
    }
}
