package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Builder
public record MemberInfoForFollowResDto(
        Long memberId,
        String nickname,
        String name,
        String profileImage,
        boolean isFollow
) {
    public static MemberInfoForFollowResDto of(Member member, boolean isFollow) {
        return MemberInfoForFollowResDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .name(member.getName())
                .profileImage(member.getPicture())
                .isFollow(isFollow)
                .build();
    }
}
