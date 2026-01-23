package shop.kkeujeok.kkeujeokbackend.notification.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.notification.domain.Notification;

@Builder
@Schema(description = "알림 정보 응답 DTO")
public record NotificationInfoResDto(
        @Schema(description = "알림 ID", example = "1")
        @NotNull(message = "알림 ID는 필수입니다.")
        Long id,

        @Schema(description = "알림 메시지", example = "새로운 댓글이 달렸습니다.")
        @NotNull(message = "알림 메시지는 필수입니다.")
        String message,

        @Schema(description = "읽음 여부", example = "false")
        @NotNull(message = "읽음 여부는 필수입니다.")
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
