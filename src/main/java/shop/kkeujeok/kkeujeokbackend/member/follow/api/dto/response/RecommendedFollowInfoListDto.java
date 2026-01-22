package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

@Builder
@Schema(description = "추천 친구 목록 응답 DTO")
public record RecommendedFollowInfoListDto(
        @Schema(description = "추천 친구 정보 리스트")
        List<RecommendedFollowInfoResDto> recommendedFollowInfoResDtos,

        @Schema(description = "페이지 정보")
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
