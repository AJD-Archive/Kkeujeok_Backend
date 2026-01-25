package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "내 팔로우 수 응답 DTO")
public record MyFollowsResDto(
        @Schema(description = "내 팔로우 수", example = "10")
        @NotNull(message = "내 팔로우 수는 필수입니다.")
        int myFollowsCount
) {
    public static MyFollowsResDto from(Integer myFollowsCount) {
        return MyFollowsResDto.builder()
                .myFollowsCount(myFollowsCount)
                .build();
    }
}
