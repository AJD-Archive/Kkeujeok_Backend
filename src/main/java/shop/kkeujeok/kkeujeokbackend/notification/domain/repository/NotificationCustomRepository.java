package shop.kkeujeok.kkeujeokbackend.notification.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.notification.domain.Notification;

public interface NotificationCustomRepository {
    Page<Notification> findAllNotifications(Member member, Pageable pageable);
}
