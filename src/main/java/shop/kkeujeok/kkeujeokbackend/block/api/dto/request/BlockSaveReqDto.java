package shop.kkeujeok.kkeujeokbackend.block.api.dto.request;

import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.Dashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

public record BlockSaveReqDto(
        Long dashboardId,
        String title,
        String contents,
        Progress progress,
        String deadLine
) {
    public Block toEntity(Member member, Dashboard dashboard) {
        return Block.builder()
                .title(title)
                .contents(contents)
                .progress(progress)
                .deadLine(deadLine)
                .member(member)
                .dashboard(dashboard)
                .build();
    }
}
