package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request;

public record TeamDocumentUpdateReqDto(
        String title,
        String content,
        String category
) {
}
