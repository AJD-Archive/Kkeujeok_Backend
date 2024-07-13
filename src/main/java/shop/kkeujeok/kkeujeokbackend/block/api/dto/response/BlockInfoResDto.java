package shop.kkeujeok.kkeujeokbackend.block.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Builder
public record BlockInfoResDto(
        String title,
        String contents,
        Progress progress,
        String nickname
) {
    public static BlockInfoResDto of(Block block, Member member) {
        return BlockInfoResDto.builder()
                .title(block.getTitle())
                .contents(block.getContents())
                .progress(block.getProgress())
                .nickname(member.getNickname())
                .build();
    }
}
