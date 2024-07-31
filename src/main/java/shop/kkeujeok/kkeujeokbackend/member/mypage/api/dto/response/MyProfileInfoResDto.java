package shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;

@Builder
public record MyProfileInfoResDto(
        String picture,
        String email,
        String name,
        String nickName,
        SocialType socialType,
        String introduce

) {
    public static MyProfileInfoResDto myProfileInfoFrom(Member member) {
        return MyProfileInfoResDto.builder()
                .picture(member.getPicture())
                .email(member.getEmail())
                .name(member.getName())
                .nickName(member.getNickname())
                .socialType(member.getSocialType())
                .introduce(member.getIntroduction())
                .build();
    }
}
