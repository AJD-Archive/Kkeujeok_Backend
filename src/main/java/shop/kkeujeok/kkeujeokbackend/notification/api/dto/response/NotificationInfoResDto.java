package shop.kkeujeok.kkeujeokbackend.notification.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.notification.domain.Notification;

@Builder
@Schema(description = "알림 정보 응답 DTO")
public record NotificationInfoResDto(
        @Schema(description = "알림 ID", example = "1")
        Long id,

        @Schema(description = "알림 메시지", example = "새로운 댓글이 달렸습니다.")
        String message,

        @Schema(description = "읽음 여부", example = "false")
        Boolean isRead
) {
    public static NotificationInfoResDto from(Notification notification) {
        return NotificationInfoResDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .build();
    }
}
