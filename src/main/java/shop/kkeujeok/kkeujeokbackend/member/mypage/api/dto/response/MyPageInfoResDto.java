package shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;

@Builder
@Schema(description = "마이페이지 정보 응답 DTO")
public record MyPageInfoResDto(
        @Schema(description = "프로필 사진 URL", example = "http://example.com/profile.jpg")
        @NotNull(message = "프로필 사진 URL은 필수입니다.")
        String picture,

        @Schema(description = "이메일", example = "user@example.com")
        @NotNull(message = "이메일은 필수입니다.")
        String email,

        @Schema(description = "이름", example = "홍길동")
        @NotNull(message = "이름은 필수입니다.")
        String name,

        @Schema(description = "닉네임", example = "길동이")
        @NotNull(message = "닉네임은 필수입니다.")
        String nickName,

        @Schema(description = "소셜 로그인 타입", example = "KAKAO")
        @NotNull(message = "소셜 로그인 타입은 필수입니다.")
        SocialType socialType,

        @Schema(description = "자기소개", example = "안녕하세요.")
        @NotNull(message = "자기소개는 필수입니다.")
        String introduction,

        @Schema(description = "회원 ID", example = "1")
        @NotNull(message = "회원 ID는 필수입니다.")
        Long memberId,

        @Schema(description = "태그", example = "#1234")
        @NotNull(message = "태그는 필수입니다.")
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
