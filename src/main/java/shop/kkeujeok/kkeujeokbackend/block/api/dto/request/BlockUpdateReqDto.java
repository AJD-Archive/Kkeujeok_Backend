package shop.kkeujeok.kkeujeokbackend.block.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "블록 수정 요청 DTO")
public record BlockUpdateReqDto(
        @Schema(description = "블록 제목", example = "수정된 프로젝트 기획")
        String title,

        @Schema(description = "블록 내용", example = "수정된 프로젝트 기획서 작성 및 검토")
        String contents,

        @Schema(description = "시작 날짜", example = "2023-10-02")
        String startDate,

        @Schema(description = "마감 기한", example = "2023-10-12")
        String deadLine
) {
}
