package shop.kkeujeok.kkeujeokbackend.block.api.dto.request;

import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.Type;
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.Dashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

public record BlockSaveReqDto(
        Long dashboardId,
        String title,
        String contents,
        Progress progress,
        String startDate,
        String deadLine
) {
    public Block toEntity(Member member, Dashboard dashboard, int lastSequence) {
        return Block.builder()
                .title(title)
                .contents(contents)
                .progress(progress)
                .type(Type.BASIC)
                .startDate(startDate)
                .deadLine(deadLine)
                .sequence(lastSequence + 1)
                .member(member)
                .dashboard(dashboard)
                .build();
    }
}
