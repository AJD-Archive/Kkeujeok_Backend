package shop.kkeujeok.kkeujeokbackend.notification.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.notification.domain.Notification;

@Builder
public record NotificationInfoResDto(
        Member receiver,
        String message,
        Boolean isRead
) {
    public static NotificationInfoResDto from(Notification notification) {
        return NotificationInfoResDto.builder()
                .receiver(notification.getReceiver())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .build();
    }

}
