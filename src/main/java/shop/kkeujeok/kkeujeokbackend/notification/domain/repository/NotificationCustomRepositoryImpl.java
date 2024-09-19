package shop.kkeujeok.kkeujeokbackend.notification.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {
}
