package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.Follow;

@Builder
public record FollowResDto(
        Long toMemberId) {
    public static FollowResDto from(Member member) {
        return FollowResDto.builder()
                .toMemberId(member.getId())
                .build();
    }
}
