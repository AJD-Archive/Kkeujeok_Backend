package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.Follow;

@Builder
public record RecommendedFollowInfoResDto(
        Long memberId,
        String nickname,
        String name,
        String profileImage
) {
    public static RecommendedFollowInfoResDto from(Member member) {
        return RecommendedFollowInfoResDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .name(member.getName())
                .profileImage(member.getPicture())
                .build();
    }
}
