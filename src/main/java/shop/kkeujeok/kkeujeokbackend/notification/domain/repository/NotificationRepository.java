package shop.kkeujeok.kkeujeokbackend.notification.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationCustomRepository {
    List<Notification> findAllByReceiver(Member member);
}
