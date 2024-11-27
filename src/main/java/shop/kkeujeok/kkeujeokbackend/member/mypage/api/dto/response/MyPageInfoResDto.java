package shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;

@Builder
public record MyPageInfoResDto(
        String picture,
        String email,
        String name,
        String nickName,
        SocialType socialType,
        String introduction,
        Long memberId

) {
    public static MyPageInfoResDto From(Member member) {
        return MyPageInfoResDto.builder()
                .picture(member.getPicture())
                .email(member.getEmail())
                .name(member.getName())
                .nickName(member.getNickname())
                .socialType(member.getSocialType())
                .introduction(member.getIntroduction())
                .memberId(member.getId())
                .build();
    }
}
