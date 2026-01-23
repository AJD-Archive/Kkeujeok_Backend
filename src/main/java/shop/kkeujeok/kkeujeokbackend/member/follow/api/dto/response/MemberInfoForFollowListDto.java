package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

@Builder
@Schema(description = "친구 검색 결과 목록 응답 DTO")
public record MemberInfoForFollowListDto(
        @Schema(description = "회원 정보 리스트")
        @NotNull(message = "회원 정보 리스트는 필수입니다.")
        List<MemberInfoForFollowResDto> memberInfoForFollowResDtos,

        @Schema(description = "페이지 정보")
        @NotNull(message = "페이지 정보는 필수입니다.")
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
