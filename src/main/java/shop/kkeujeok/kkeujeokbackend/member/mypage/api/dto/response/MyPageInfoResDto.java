package shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;

@Builder
@Schema(description = "마이페이지 정보 응답 DTO")
public record MyPageInfoResDto(
        @Schema(description = "프로필 사진 URL", example = "http://example.com/profile.jpg")
        String picture,

        @Schema(description = "이메일", example = "user@example.com")
        String email,

        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "닉네임", example = "길동이")
        String nickName,

        @Schema(description = "소셜 로그인 타입", example = "KAKAO")
        SocialType socialType,

        @Schema(description = "자기소개", example = "안녕하세요.")
        String introduction,

        @Schema(description = "회원 ID", example = "1")
        Long memberId,

        @Schema(description = "태그", example = "#1234")
        String tag
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
                .tag(member.getTag())
                .build();
    }
}
