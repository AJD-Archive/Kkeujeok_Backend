package shop.kkeujeok.kkeujeokbackend.block.api.dto.response;

import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

@Builder
public record BlockListResDto(
        List<BlockInfoResDto> blockListResDto,
        PageInfoResDto pageInfoResDto
) {
    public static BlockListResDto from(List<BlockInfoResDto> blocks, PageInfoResDto pageInfoResDto) {
        return BlockListResDto.builder()
                .blockListResDto(blocks)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
