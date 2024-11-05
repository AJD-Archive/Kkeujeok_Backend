package shop.kkeujeok.kkeujeokbackend.member.follow.domain.repository;

import static shop.kkeujeok.kkeujeokbackend.member.follow.domain.QFollow.follow;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.FollowInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.FollowStatus;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowCustomRepositoryImpl implements FollowCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

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

    @Override
    @Transactional
    public void acceptFollowingRequest(Long followId) {
        new JPAUpdateClause(entityManager, follow)
                .where(follow.id.eq(followId))
                .set(follow.followStatus, FollowStatus.ACCEPT)
                .execute();
    }

    @Override
    public Page<FollowInfoResDto> findFollowList(Long memberId, Pageable pageable) {
        List<FollowInfoResDto> fetch = queryFactory
                .selectFrom(follow)
                .where(follow.fromMember.id.eq(memberId)
                        .or(follow.toMember.id.eq(memberId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(follow -> FollowInfoResDto.from(follow, memberId))
                .collect(Collectors.toList());

        long total = Optional.ofNullable(queryFactory
                .select(follow.count())
                .from(follow)
                .where(follow.fromMember.id.eq(memberId)
                        .or(follow.toMember.id.eq(memberId)))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(fetch, pageable, total);
    }
}
