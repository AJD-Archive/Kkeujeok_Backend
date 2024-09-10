package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Page;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.domain.TeamDocument;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

import java.util.List;

@Builder
public record FindTeamDocumentResDto(
        List<TeamDocumentResDto> teamDocuments,
        PageInfoResDto pageInfoResDto
) {
    public static FindTeamDocumentResDto from(List<TeamDocumentResDto> teamDocuments, PageInfoResDto pageInfoResDto) {
        return FindTeamDocumentResDto.builder()
                .teamDocuments(teamDocuments)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
