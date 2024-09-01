package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;

@Builder
public record DocumentInfoResDto(
        Long documentId,
        String title
) {
    public static DocumentInfoResDto from(Document document) {
        return DocumentInfoResDto.builder()
                .documentId(document.getId())
                .title(document.getTitle())
                .build();
    }
}
