package shop.kkeujeok.kkeujeokbackend.notification.api.dto.response;

import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

@Builder
public record NotificationListResDto(
        List<NotificationInfoResDto> notificationInfoResDto,
        PageInfoResDto pageInfoResDto

) {
    public static NotificationListResDto of(List<NotificationInfoResDto> notificationInfoResDtoList,
                                            PageInfoResDto pageInfoResDto) {
        return NotificationListResDto.builder()
                .notificationInfoResDto(notificationInfoResDtoList)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
