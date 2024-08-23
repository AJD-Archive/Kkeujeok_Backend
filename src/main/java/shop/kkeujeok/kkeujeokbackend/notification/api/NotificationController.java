package shop.kkeujeok.kkeujeokbackend.notification.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;
import shop.kkeujeok.kkeujeokbackend.notification.api.dto.response.NotificationInfoResDto;
import shop.kkeujeok.kkeujeokbackend.notification.api.dto.response.NotificationListResDto;
import shop.kkeujeok.kkeujeokbackend.notification.application.NotificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/stream")
    public SseEmitter streamNotifications(@CurrentUserEmail String email) {
        return notificationService.createEmitter(email);
    }

    @GetMapping
    public RspTemplate<NotificationListResDto> getAllNotifications(@CurrentUserEmail String email, Pageable pageable) {
        NotificationListResDto notificationList = notificationService.findAllNotificationsFromMember(email, pageable);
        return new RspTemplate<>(HttpStatus.OK, "알림 조회 성공", notificationList);
    }

    @GetMapping("/{notificationId}")
    public RspTemplate<NotificationInfoResDto> getNotificationById(@PathVariable Long notificationId) {
        NotificationInfoResDto notification = notificationService.findByNotificationId(notificationId);
        return new RspTemplate<>(HttpStatus.OK, "알림 상세 조회 성공", notification);
    }
}
