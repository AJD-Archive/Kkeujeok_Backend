package shop.kkeujeok.kkeujeokbackend.dashboard.domain.repository;

import static shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.QPersonalDashboard.personalDashboard;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Repository
@Transactional(readOnly = true)
public class DashboardCustomRepositoryImpl implements DashboardCustomRepository {

    private final JPAQueryFactory queryFactory;

    public DashboardCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<PersonalDashboard> findForPersonalDashboard(Member member, Pageable pageable) {
        long total = queryFactory
                .selectFrom(personalDashboard)
                .where(personalDashboard._super.member.eq(member))
                .stream()
                .count();

        List<PersonalDashboard> dashboards = queryFactory
                .selectFrom(personalDashboard)
                .where(personalDashboard._super.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(dashboards, pageable, total);
    }
}
