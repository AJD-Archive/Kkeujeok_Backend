package shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

@Builder
@Schema(description = "친구 목록 응답 DTO")
public record FollowInfoListDto(
        @Schema(description = "친구 정보 리스트")
        @NotNull(message = "친구 정보 리스트는 필수입니다.")
        List<FollowInfoResDto> followInfoResDto,

        @Schema(description = "페이지 정보")
        @NotNull(message = "페이지 정보는 필수입니다.")
        PageInfoResDto pageInfoResDto
) {
    public static FollowInfoListDto of(List<FollowInfoResDto> follows, PageInfoResDto pageInfoResDto) {
        return FollowInfoListDto.builder()
                .followInfoResDto(follows)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
