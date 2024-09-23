package shop.kkeujeok.kkeujeokbackend.notification.api.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record NotificationListResDto(
        List<NotificationInfoResDto> notificationInfoResDto

) {
    public static NotificationListResDto of(List<NotificationInfoResDto> notificationInfoResDtoList) {
        return NotificationListResDto.builder()
                .notificationInfoResDto(notificationInfoResDtoList)
                .build();
    }
}
