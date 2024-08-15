package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record DocumentListResDto(
        List<DocumentInfoResDto> documentInfoResDtos
) {
    public static DocumentListResDto from(List<DocumentInfoResDto> documentInfoResDtos) {
        return DocumentListResDto.builder()
                .documentInfoResDtos(documentInfoResDtos)
                .build();
    }
}
