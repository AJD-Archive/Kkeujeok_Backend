package shop.kkeujeok.kkeujeokbackend.admin.api.response;

import java.util.List;
import lombok.Builder;

@Builder
public record NoticeListResDto(
        List<NoticeInfoResDto> noticeListResDto
) {
    public static NoticeListResDto from(List<NoticeInfoResDto> notices) {
        return NoticeListResDto.builder()
                .noticeListResDto(notices)
                .build();
    }
}
