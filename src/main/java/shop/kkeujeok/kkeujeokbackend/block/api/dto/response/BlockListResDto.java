package shop.kkeujeok.kkeujeokbackend.block.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

@Builder
@Schema(description = "블록 목록 응답 DTO")
public record BlockListResDto(
        @Schema(description = "블록 정보 리스트")
        @NotNull(message = "블록 정보 리스트는 필수입니다.")
        List<BlockInfoResDto> blockListResDto,

        @Schema(description = "페이지 정보")
        @NotNull(message = "페이지 정보는 필수입니다.")
        PageInfoResDto pageInfoResDto
) {
    public static BlockListResDto from(List<BlockInfoResDto> blocks, PageInfoResDto pageInfoResDto) {
        return BlockListResDto.builder()
                .blockListResDto(blocks)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
