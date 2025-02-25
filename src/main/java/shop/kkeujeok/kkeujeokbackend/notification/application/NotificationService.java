package shop.kkeujeok.kkeujeokbackend.notification.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.exception.MemberNotFoundException;
import shop.kkeujeok.kkeujeokbackend.notification.api.dto.response.NotificationInfoResDto;
import shop.kkeujeok.kkeujeokbackend.notification.api.dto.response.NotificationListResDto;
import shop.kkeujeok.kkeujeokbackend.notification.domain.Notification;
import shop.kkeujeok.kkeujeokbackend.notification.domain.repository.NotificationRepository;
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

        sseEmitterManager.send(member.getId(), savedNotification.getMessage());
    }

    @Transactional(readOnly = true)
    public NotificationListResDto findAllNotificationsFromMember(String email) {
        Member member = findMemberByEmail(email);
        List<NotificationInfoResDto> notifications = notificationRepository.findAllByReceiver(member)
                .stream()
                .map(NotificationInfoResDto::from)
                .toList();

        return NotificationListResDto.of(notifications);
    }

    @Transactional
    public void markAllNotificationsAsRead(String email) {
        Member member = findMemberByEmail(email);
        List<Notification> notifications = notificationRepository.findAllByReceiver(member);

        notifications.forEach(Notification::markAsRead);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }
}
