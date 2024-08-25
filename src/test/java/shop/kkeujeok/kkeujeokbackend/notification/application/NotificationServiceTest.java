package shop.kkeujeok.kkeujeokbackend.notification.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.Role;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.notification.api.dto.response.NotificationListResDto;
import shop.kkeujeok.kkeujeokbackend.notification.domain.Notification;
import shop.kkeujeok.kkeujeokbackend.notification.domain.repository.NotificationRepository;
import shop.kkeujeok.kkeujeokbackend.notification.exception.NotificationNotFoundException;
import shop.kkeujeok.kkeujeokbackend.notification.util.SseEmitterManager;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private SseEmitter emitter;

    @Mock
    private SseEmitterManager sseEmitterManager;

    @InjectMocks
    private NotificationService notificationService;

    private Member member;
    private Notification notification;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .status(Status.ACTIVE)
                .email("kkeujeok@gmail.com")
                .name("김동균")
                .picture("기본 프로필")
                .socialType(SocialType.GOOGLE)
                .role(Role.ROLE_USER)
                .firstLogin(true)
                .nickname("동동")
                .build();

        notification = Notification.builder()
                .receiver(member)
                .message("Test Notification")
                .isRead(false)
                .build();

        emitter = new SseEmitter(Long.MAX_VALUE);
    }

    @Test
    @DisplayName("회원은 SSE 발행기를 생성할 수 있다")
    void 회원은_SSE_발행기를_생성할_수_있다() {
        // given
        when(memberRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(member));

        when(sseEmitterManager.createEmitter(member.getId()))
                .thenReturn(emitter);

        // when
        SseEmitter result = notificationService.createEmitter(member.getEmail());

        // then
        assertThat(result).isNotNull();
    }


    @Test
    @DisplayName("알림을 회원에게 보낼 수 있다")
    void 알림을_회원에게_보낼_수_있다() {
        // given
        when(notificationRepository.save(any(Notification.class)))
                .thenReturn(notification);

        // when
        notificationService.sendNotification(member, "새로운 알림");

        // then
        // 어떻게 테스트할 지 고민해보겠습니다..
    }

    @Test
    @DisplayName("존재하지 않는 알림을 조회하면 예외가 발생한다")
    void 존재하지_않는_알림을_조회하면_예외가_발생한다() {
        // given
        Long nonExistentNotificationId = 999L;
        when(notificationRepository.findById(nonExistentNotificationId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> notificationService.findByNotificationId(nonExistentNotificationId))
                .isInstanceOf(NotificationNotFoundException.class);
    }

    @Test
    @DisplayName("회원의 모든 알림을 조회할 수 있다")
    void 회원의_모든_알림을_조회할_수_있다() {
        // given
        List<Notification> notifications = List.of(notification);
        when(notificationRepository.findAllNotifications(any(Member.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(notifications));
        when(memberRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(member));

        // when
        NotificationListResDto result = notificationService.findAllNotificationsFromMember(member.getEmail(),
                Pageable.unpaged());

        // then
        assertThat(result.notificationInfoResDto()).isNotEmpty();
        assertThat(result.pageInfoResDto().totalItems()).isEqualTo(notifications.size());
    }
}
