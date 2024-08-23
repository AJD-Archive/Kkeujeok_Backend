package shop.kkeujeok.kkeujeokbackend.notification.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.notification.domain.Notification;
import shop.kkeujeok.kkeujeokbackend.notification.domain.QNotification;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Notification> findAllNotifications(Member member, Pageable pageable) {
        QNotification notification = QNotification.notification;

        long total = Optional.ofNullable(
                queryFactory
                        .select(notification.count())
                        .from(notification)
                        .where(notification.receiver.eq(member))
                        .fetchOne()
        ).orElse(0L);

        List<Notification> notifications = queryFactory
                .selectFrom(notification)
                .where(notification.receiver.eq(member))
                .orderBy(notification.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(notifications, pageable, total);
    }


}
