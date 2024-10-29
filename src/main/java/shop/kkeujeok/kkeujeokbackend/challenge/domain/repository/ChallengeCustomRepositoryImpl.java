package shop.kkeujeok.kkeujeokbackend.challenge.domain.repository;

import static shop.kkeujeok.kkeujeokbackend.challenge.domain.QChallenge.challenge;
import static shop.kkeujeok.kkeujeokbackend.challenge.domain.QChallengeMemberMapping.challengeMemberMapping;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSearchReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Category;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeCustomRepositoryImpl implements ChallengeCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Challenge> findAllChallenges(Pageable pageable) {
        long total = Optional.ofNullable(
                queryFactory
                        .select(challenge.count())
                        .from(challenge)
                        .where(challenge.status.eq(Status.ACTIVE))
                        .fetchOne()
        ).orElse(0L);

        List<Challenge> challenges = queryFactory
                .selectFrom(challenge)
                .where(challenge.status.eq(Status.ACTIVE))
                .orderBy(challenge.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(challenges, pageable, total);
    }

    @Override
    public Page<Challenge> findChallengesByMemberInMapping(Member member, Pageable pageable) {
        long total = queryFactory
                .selectFrom(challengeMemberMapping)
                .where(challengeMemberMapping.member.eq(member))
                .stream()
                .count();

        List<Challenge> challenges = queryFactory
                .select(challengeMemberMapping.challenge)
                .from(challengeMemberMapping)
                .where(challengeMemberMapping.member.eq(member)
                        .and(challengeMemberMapping.challenge.status.eq(Status.ACTIVE)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(challenges, pageable, total);
    }


    @Override
    public Page<Challenge> findChallengesByCategoryAndKeyword(ChallengeSearchReqDto challengeSearchReqDto,
                                                              Pageable pageable) {
        String keyword = challengeSearchReqDto.keyWord();
        String category = challengeSearchReqDto.category();

        BooleanExpression predicate = challenge.status.eq(Status.ACTIVE);

        if (keyword != null && !keyword.isEmpty()) {
            predicate = predicate.and(challenge.title.containsIgnoreCase(keyword));
        }

        if (category != null && !category.isEmpty()) {
            predicate = predicate.and(challenge.category.eq(Category.valueOf(category)));
        }

        long total = Optional.ofNullable(
                queryFactory
                        .select(challenge.count())
                        .from(challenge)
                        .where(predicate)
                        .fetchOne()
        ).orElse(0L);

        List<Challenge> challenges = queryFactory
                .selectFrom(challenge)
                .where(predicate)
                .orderBy(challenge.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(challenges, pageable, total);
    }
}
