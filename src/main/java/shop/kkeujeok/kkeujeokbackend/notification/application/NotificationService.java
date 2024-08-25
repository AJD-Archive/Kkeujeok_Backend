package shop.kkeujeok.kkeujeokbackend.notification.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.exception.MemberNotFoundException;
import shop.kkeujeok.kkeujeokbackend.notification.api.dto.response.NotificationInfoResDto;
import shop.kkeujeok.kkeujeokbackend.notification.api.dto.response.NotificationListResDto;
import shop.kkeujeok.kkeujeokbackend.notification.domain.Notification;
import shop.kkeujeok.kkeujeokbackend.notification.domain.repository.NotificationRepository;
import shop.kkeujeok.kkeujeokbackend.notification.exception.NotificationNotFoundException;
import shop.kkeujeok.kkeujeokbackend.notification.util.SseEmitterManager;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final SseEmitterManager sseEmitterManager;
    private final NotificationRepository notificationRepository;

    public SseEmitter createEmitter(String email) {
        Member member = findMemberByEmail(email);

        return sseEmitterManager.createEmitter(member.getId());
    }

    @Transactional
    public void sendNotification(Member member, String message) {
        Notification notification = Notification.builder()
                .receiver(member)
                .message(message)
                .isRead(false)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        sseEmitterManager.sendNotification(member.getId(), savedNotification.getMessage());
    }

    @Transactional(readOnly = true)
    public NotificationListResDto findAllNotificationsFromMember(String email, Pageable pageable) {
        Member member = findMemberByEmail(email);
        Page<Notification> notifications = notificationRepository.findAllNotifications(member, pageable);

        List<NotificationInfoResDto> notificationList = notifications.stream()
                .map(NotificationInfoResDto::from)
                .toList();

        return NotificationListResDto.of(notificationList, PageInfoResDto.from(notifications));
    }

    @Transactional
    public NotificationInfoResDto findByNotificationId(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);
        notification.markAsRead();

        return NotificationInfoResDto.from(notification);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }
}
