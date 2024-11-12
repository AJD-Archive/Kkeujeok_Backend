package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

@Builder
public record MemberInfoForFollowListDto(
        List<MemberInfoForFollowResDto> memberInfoForFollowResDtos,
        PageInfoResDto pageInfoResDto
) {
    public static MemberInfoForFollowListDto of(List<MemberInfoForFollowResDto> infoResDtos,
                                                PageInfoResDto pageInfoResDto) {
        return MemberInfoForFollowListDto.builder()
                .memberInfoForFollowResDtos(infoResDtos)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
