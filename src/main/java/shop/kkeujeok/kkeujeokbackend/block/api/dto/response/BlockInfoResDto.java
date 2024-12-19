package shop.kkeujeok.kkeujeokbackend.block.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.Type;

@Builder
public record BlockInfoResDto(
        Long blockId,
        String title,
        String contents,
        Progress progress,
        Type type,
        String dType,
        String startDate,
        String deadLine,
        String nickname,
        String picture,
        String dDay
) {
    public static BlockInfoResDto from(Block block, String dDay) {
        return new BlockInfoResDto(
                block.getId(),
                block.getTitle(),
                block.getContents(),
                block.getProgress(),
                block.getType(),
                block.getDashboard().getDType(),
                block.getStartDate(),
                block.getDeadLine(),
                block.getMember().getNickname(),
                block.getMember().getPicture(),
                dDay
        );
    }

}
