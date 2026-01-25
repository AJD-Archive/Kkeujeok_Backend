package shop.kkeujeok.kkeujeokbackend.block.api.dto.request;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.Type;
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.Dashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Schema(description = "블록 생성 요청 DTO")
public record BlockSaveReqDto(
        @Schema(description = "대시보드 ID", example = "1")
        @NotNull(message = "대시보드 ID는 필수입니다.")
        Long dashboardId,

        @Schema(description = "블록 제목", example = "프로젝트 기획")
        @NotNull(message = "블록 제목은 필수입니다.")
        String title,

        @Schema(description = "블록 내용", example = "프로젝트 기획서 작성 및 검토")
        @NotNull(message = "블록 내용은 필수입니다.")
        String contents,

        @Schema(description = "진행 상태", example = "NOT_STARTED")
        @NotNull(message = "진행 상태는 필수입니다.")
        Progress progress,

        @Schema(description = "시작 날짜", example = "2023-10-01")
        @NotNull(message = "시작 날짜는 필수입니다.")
        String startDate,

        @Schema(description = "마감 기한", example = "2023-10-10")
        @NotNull(message = "마감 기한은 필수입니다.")
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
