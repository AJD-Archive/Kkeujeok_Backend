package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.domain.TeamDocument;

@Builder
@Schema(description = "팀 문서 응답 DTO")
public record TeamDocumentResDto(
        @Schema(description = "작성자", example = "홍길동")
        String author,

        @Schema(description = "작성자 프로필 사진 URL", example = "http://example.com/profile.jpg")
        String picture,

        @Schema(description = "문서 제목", example = "팀 회의록")
        String title,

        @Schema(description = "문서 카테고리", example = "회의록")
        String category,

        @Schema(description = "팀 문서 ID", example = "1")
        Long teamDocumentId
) {
    public static TeamDocumentResDto from(TeamDocument document) {
        return TeamDocumentResDto.builder()
                .author(document.getAuthor())
                .picture(document.getPicture())
                .title(document.getTitle())
                .category(document.getCategory())
                .teamDocumentId(document.getId())
                .build();
    }
}
