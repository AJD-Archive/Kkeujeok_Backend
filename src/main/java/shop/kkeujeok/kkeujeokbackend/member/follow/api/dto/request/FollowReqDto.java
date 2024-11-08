package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.request;

import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.Follow;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.FollowStatus;

public record FollowReqDto(
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
