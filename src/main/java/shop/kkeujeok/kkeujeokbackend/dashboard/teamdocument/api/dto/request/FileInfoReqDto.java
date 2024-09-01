package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.request;

import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.File;

public record FileInfoReqDto(
        Long documentId,
        String email,
        String title,
        String content
) {
    public File toEntity(String email, Document document) {
        return File.builder()
                .email(email)
                .title(title)
                .content(content)
                .document(document)
                .build();
    }
}
