package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

@Builder
public record FollowInfoListDto(
        List<FollowInfoResDto> followInfoResDto,
        PageInfoResDto pageInfoResDto
) {
    public static FollowInfoListDto from(List<FollowInfoResDto> follows, PageInfoResDto pageInfoResDto) {
        return FollowInfoListDto.builder()
                .followInfoResDto(follows)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
