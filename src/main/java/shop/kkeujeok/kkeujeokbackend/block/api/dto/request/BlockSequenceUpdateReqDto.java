package shop.kkeujeok.kkeujeokbackend.block.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "블록 순서 변경 요청 DTO")
public record BlockSequenceUpdateReqDto(
        @Schema(description = "대시보드 ID", example = "1")
        Long dashboardId,

        @Schema(description = "시작 전 상태의 블록 ID 리스트", example = "[1, 2, 3]")
        List<Long> notStartedList,

        @Schema(description = "진행 중 상태의 블록 ID 리스트", example = "[4, 5]")
        List<Long> inProgressList,

        @Schema(description = "완료 상태의 블록 ID 리스트", example = "[6, 7, 8]")
        List<Long> completedList
) {
}
