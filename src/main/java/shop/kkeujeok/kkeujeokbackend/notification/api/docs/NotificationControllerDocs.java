package shop.kkeujeok.kkeujeokbackend.notification.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;
import shop.kkeujeok.kkeujeokbackend.notification.api.dto.response.NotificationListResDto;

@Tag(name = "NotificationController", description = "알림 관련 API")
public interface NotificationControllerDocs {

    @Operation(summary = "알림 스트림 연결", description = "SSE를 통해 실시간 알림을 수신합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 스트림 연결 성공")
    })
    SseEmitter streamNotifications(@CurrentUserEmail String email);

    @Operation(summary = "알림 전체 조회", description = "사용자의 모든 알림을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 전체 조회 성공")
    })
    RspTemplate<NotificationListResDto> findAllNotifications(@CurrentUserEmail String email);

    @Operation(summary = "모든 알림 읽음 처리", description = "사용자의 모든 알림을 읽음 상태로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 알림 읽음 처리 성공")
    })
    RspTemplate<Void> markAllNotificationsAsRead(@CurrentUserEmail String email);
}
