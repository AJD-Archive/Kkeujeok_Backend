package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

@Builder
public record RecommendedFollowInfoListDto(
        List<RecommendedFollowInfoResDto> recommendedFollowInfoResDtos,
        PageInfoResDto pageInfoResDto
) {
    public static RecommendedFollowInfoListDto of(List<RecommendedFollowInfoResDto> infoResDtos,
                                                  PageInfoResDto pageInfoResDto) {
        return RecommendedFollowInfoListDto.builder()
                .recommendedFollowInfoResDtos(infoResDtos)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
