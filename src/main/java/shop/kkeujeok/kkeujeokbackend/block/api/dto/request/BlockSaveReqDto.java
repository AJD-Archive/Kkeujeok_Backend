package shop.kkeujeok.kkeujeokbackend.block.api.dto.request;

import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

public record BlockSaveReqDto(
        String title,
        String contents,
        Progress progress
) {
    public Block toEntity(Member member) {
        return Block.builder()
                .title(title)
                .contents(contents)
                .progress(progress)
                .member(member)
                .build();
    }
}
