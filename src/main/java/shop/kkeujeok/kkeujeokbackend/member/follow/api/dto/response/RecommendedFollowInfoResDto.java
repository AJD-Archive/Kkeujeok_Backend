package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.Follow;

@Builder
@Schema(description = "추천 친구 정보 응답 DTO")
public record RecommendedFollowInfoResDto(
        @Schema(description = "회원 ID", example = "4")
        @NotNull(message = "회원 ID는 필수입니다.")
        Long memberId,

        @Schema(description = "닉네임", example = "영희")
        @NotNull(message = "닉네임은 필수입니다.")
        String nickname,

        @Schema(description = "이름", example = "이영희")
        @NotNull(message = "이름은 필수입니다.")
        String name,

        @Schema(description = "프로필 이미지 URL", example = "http://example.com/profile.jpg")
        @NotNull(message = "프로필 이미지 URL은 필수입니다.")
        String profileImage,

        @Schema(description = "팔로우 여부", example = "false")
        @NotNull(message = "팔로우 여부는 필수입니다.")
        boolean isFollow

) {
    public static RecommendedFollowInfoResDto from(Member member, boolean isFollow) {
        return RecommendedFollowInfoResDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .name(member.getName())
                .profileImage(member.getPicture())
                .isFollow(isFollow)
                .build();
    }
}
