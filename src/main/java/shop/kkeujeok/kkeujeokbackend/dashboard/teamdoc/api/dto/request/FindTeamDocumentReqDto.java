package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "팀 문서 검색 요청 DTO")
public record FindTeamDocumentReqDto(
        @Schema(description = "검색할 카테고리", example = "회의록")
        @NotNull(message = "검색할 카테고리는 필수입니다.")
        String category
) {
    public static FindTeamDocumentReqDto of(String category) {
        return FindTeamDocumentReqDto.builder()
                .category(category)
                .build();
    }
}
