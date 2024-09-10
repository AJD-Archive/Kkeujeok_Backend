package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request;

import lombok.Builder;

@Builder
public record FindTeamDocumentReqDto(
        String category
) {
    public static FindTeamDocumentReqDto of(String category) {
        return FindTeamDocumentReqDto.builder()
                .category(category)
                .build();
    }
}
