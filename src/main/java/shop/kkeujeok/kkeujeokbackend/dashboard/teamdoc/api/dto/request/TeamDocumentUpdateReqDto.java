package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "팀 문서 수정 요청 DTO")
public record TeamDocumentUpdateReqDto(
        @Schema(description = "문서 제목", example = "수정된 팀 회의록")
        @NotNull(message = "문서 제목은 필수입니다.")
        String title,

        @Schema(description = "문서 내용", example = "수정된 회의 내용입니다.")
        @NotNull(message = "문서 내용은 필수입니다.")
        String content,

        @Schema(description = "문서 카테고리", example = "업무 일지")
        @NotNull(message = "문서 카테고리는 필수입니다.")
        String category
) {
}
