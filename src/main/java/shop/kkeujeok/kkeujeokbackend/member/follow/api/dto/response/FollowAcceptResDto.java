package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.Follow;

@Builder
public record FollowAcceptResDto(
        Long fromMemberId,
        Long toMemberId
) {
    public static FollowAcceptResDto from(Follow follow) {
        return FollowAcceptResDto.builder()
                .fromMemberId(follow.getFromMember().getId())
                .toMemberId(follow.getToMember().getId())
                .build();
    }
}
