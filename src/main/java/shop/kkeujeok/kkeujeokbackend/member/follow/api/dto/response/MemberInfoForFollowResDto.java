package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Builder
@Schema(description = "친구 검색 결과 회원 정보 DTO")
public record MemberInfoForFollowResDto(
        @Schema(description = "회원 ID", example = "3")
        @NotNull(message = "회원 ID는 필수입니다.")
        Long memberId,

        @Schema(description = "닉네임", example = "철수")
        @NotNull(message = "닉네임은 필수입니다.")
        String nickname,

        @Schema(description = "이름", example = "김철수")
        @NotNull(message = "이름은 필수입니다.")
        String name,

        @Schema(description = "프로필 이미지 URL", example = "http://example.com/profile.jpg")
        @NotNull(message = "프로필 이미지 URL은 필수입니다.")
        String profileImage,

        @Schema(description = "팔로우 여부", example = "false")
        @NotNull(message = "팔로우 여부는 필수입니다.")
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
