package shop.kkeujeok.kkeujeokbackend.notification.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.notification.domain.Notification;

@Builder
public record NotificationInfoResDto(
        Long id,
        String message,
        Boolean isRead
) {
    public static NotificationInfoResDto from(Notification notification) {
        notification.markAsRead();
        
        return NotificationInfoResDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .build();
    }
}
