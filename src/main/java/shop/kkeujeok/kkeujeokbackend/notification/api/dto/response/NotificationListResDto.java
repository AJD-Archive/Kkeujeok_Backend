package shop.kkeujeok.kkeujeokbackend.notification.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "알림 목록 응답 DTO")
public record NotificationListResDto(
        @Schema(description = "알림 정보 리스트")
        List<NotificationInfoResDto> notificationInfoResDto

) {
    public static NotificationListResDto of(List<NotificationInfoResDto> notificationInfoResDtoList) {
        return NotificationListResDto.builder()
                .notificationInfoResDto(notificationInfoResDtoList)
                .build();
    }
}
