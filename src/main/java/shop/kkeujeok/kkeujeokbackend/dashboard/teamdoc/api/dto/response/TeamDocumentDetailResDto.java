package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.domain.TeamDocument;

@Builder
@Schema(description = "팀 문서 상세 정보 응답 DTO")
public record TeamDocumentDetailResDto(
        @Schema(description = "작성자", example = "홍길동")
        @NotNull(message = "작성자는 필수입니다.")
        String author,

        @Schema(description = "작성자 프로필 사진 URL", example = "http://example.com/profile.jpg")
        @NotNull(message = "작성자 프로필 사진 URL은 필수입니다.")
        String picture,

        @Schema(description = "문서 제목", example = "팀 회의록")
        @NotNull(message = "문서 제목은 필수입니다.")
        String title,

        @Schema(description = "문서 내용", example = "오늘 회의에서는 다음 스프린트 계획을 논의했습니다.")
        @NotNull(message = "문서 내용은 필수입니다.")
        String content,

        @Schema(description = "문서 카테고리", example = "회의록")
        @NotNull(message = "문서 카테고리는 필수입니다.")
        String category,

        @Schema(description = "팀 문서 ID", example = "1")
        @NotNull(message = "팀 문서 ID는 필수입니다.")
        Long teamDocumentId
) {
    public static TeamDocumentDetailResDto from(TeamDocument document) {
        return TeamDocumentDetailResDto.builder()
                .author(document.getAuthor())
                .picture(document.getPicture())
                .title(document.getTitle())
                .content(document.getContent())
                .category(document.getCategory())
                .teamDocumentId(document.getId())
                .build();
    }
}
