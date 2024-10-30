package shop.kkeujeok.kkeujeokbackend.member.follow.domain.repository;

import static shop.kkeujeok.kkeujeokbackend.member.follow.domain.QFollow.follow;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowCustomRepositoryImpl implements FollowCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByFromMemberAndToMember(Member fromMember, Member toMember) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(follow)
                .where((follow.fromMember.eq(fromMember)
                        .and(follow.toMember.eq(toMember)))
                        .or(follow.fromMember.eq(toMember)
                                .and(follow.toMember.eq(fromMember))))
                .fetchFirst();

        return fetchOne != null;
    }
}
