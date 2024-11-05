package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.Follow;

@Builder
public record FollowInfoResDto(
        Long memberId,
        String nickname,
        String name,
        String profileImage
) {
    public static FollowInfoResDto from(Follow follow, Long myMemberId) {
        Member friend = follow.getToMember().getId().equals(myMemberId)
                ? follow.getFromMember()
                : follow.getToMember();

        return FollowInfoResDto.builder()
                .memberId(friend.getId())
                .nickname(friend.getNickname())
                .name(friend.getName())
                .profileImage(friend.getPicture())
                .build();
    }
}
