package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

import java.util.List;

@Builder
public record DocumentListResDto(
        List<DocumentInfoResDto> documentInfoResDtos,
        PageInfoResDto pageInfoResDto
) {
    public static DocumentListResDto from(List<DocumentInfoResDto> documentInfoResDtos, PageInfoResDto pageInfoResDto) {
        return DocumentListResDto.builder()
                .documentInfoResDtos(documentInfoResDtos)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
