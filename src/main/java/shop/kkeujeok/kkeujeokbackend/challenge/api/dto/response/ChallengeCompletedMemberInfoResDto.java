package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Builder
@Schema(description = "챌린지 완료 멤버 정보 응답 DTO")
public record ChallengeCompletedMemberInfoResDto(
        @Schema(description = "회원 ID", example = "1")
        Long memberId,

        @Schema(description = "이메일", example = "user@example.com")
        String email,

        @Schema(description = "프로필 사진 URL", example = "http://example.com/profile.jpg")
        String picture,

        @Schema(description = "닉네임", example = "홍길동")
        String nickname
) {
    public static ChallengeCompletedMemberInfoResDto from(Member member) {
        return ChallengeCompletedMemberInfoResDto.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .picture(member.getPicture())
                .nickname(member.getNickname())
                .build();
    }
}
