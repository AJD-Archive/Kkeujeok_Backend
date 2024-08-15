package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.request;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Builder
public record DocumentInfoReqDto(
        String title
        // 팀 대시보드 추가
) {
    public Document toEntity(){
        return Document.builder()
                .title(title)
                .build();
    }
}
