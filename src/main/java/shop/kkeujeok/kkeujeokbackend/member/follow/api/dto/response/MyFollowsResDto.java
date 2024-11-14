package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import lombok.Builder;

@Builder
public record MyFollowsResDto(
        Integer myFollowsCount
) {
    public static MyFollowsResDto from(Integer myFollowsCount) {
        return MyFollowsResDto.builder()
                .myFollowsCount(myFollowsCount)
                .build();
    }
}
