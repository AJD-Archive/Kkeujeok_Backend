package shop.kkeujeok.kkeujeokbackend.notification.application;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final Map<Long, SseEmitter> memberEmitters = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final NotificationRepository notificationRepository;

    public SseEmitter createEmitter(String email) {
        Member member = findMemberByEmail(email);

        Long memberId = member.getId();

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        memberEmitters.put(memberId, emitter);

        emitter.onCompletion(() -> memberEmitters.remove(memberId));
        emitter.onTimeout(() -> memberEmitters.remove(memberId));
        emitter.onError((e) -> memberEmitters.remove(memberId));

        return emitter;
    }

    @Transactional
    public void sendNotification(Member member, String message) {
        Notification notification = Notification.builder()
                .receiver(member)
                .message(message)
                .isRead(false)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        sendRealTimeNotification(savedNotification);
    }

    private void sendRealTimeNotification(Notification notification) {
        SseEmitter emitter = memberEmitters.get(notification.getReceiver().getId());

        if (emitter != null) {
            executor.execute(() -> {
                try {
                    emitter.send(SseEmitter.event().name("notification").data(notification.getMessage()));
                } catch (Exception e) {
                    log.error("알림 ID: {}를 회원 ID: {}에게 전송하는 데 실패했습니다. 오류: {}",
                            notification.getId(), notification.getReceiver().getId(), e.getMessage(), e);
                }
            });
        }
        log.warn("회원 ID: {}에 대한 활성화된 Emitter가 없습니다. 알림 ID: {}를 전송할 수 없습니다.",
                notification.getReceiver().getId(), notification.getId());

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
