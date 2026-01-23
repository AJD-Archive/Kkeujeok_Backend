package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.data.domain.Page;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.domain.TeamDocument;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

import java.util.List;

@Builder
@Schema(description = "팀 문서 검색 결과 응답 DTO")
public record FindTeamDocumentResDto(
        @Schema(description = "팀 문서 리스트")
        @NotNull(message = "팀 문서 리스트는 필수입니다.")
        List<TeamDocumentResDto> teamDocuments,

        @Schema(description = "페이지 정보")
        @NotNull(message = "페이지 정보는 필수입니다.")
        PageInfoResDto pageInfoResDto
) {
    public static FindTeamDocumentResDto from(List<TeamDocumentResDto> teamDocuments, PageInfoResDto pageInfoResDto) {
        return FindTeamDocumentResDto.builder()
                .teamDocuments(teamDocuments)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
