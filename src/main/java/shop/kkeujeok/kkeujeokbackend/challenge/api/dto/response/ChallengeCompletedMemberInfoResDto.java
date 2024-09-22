package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Builder
public record ChallengeCompletedMemberInfoResDto(
        Long memberId,
        String picture,
        String nickname
) {
    public static ChallengeCompletedMemberInfoResDto from(Member member) {
        return ChallengeCompletedMemberInfoResDto.builder()
                .memberId(member.getId())
                .picture(member.getPicture())
                .nickname(member.getNickname())
                .build();
    }
}
