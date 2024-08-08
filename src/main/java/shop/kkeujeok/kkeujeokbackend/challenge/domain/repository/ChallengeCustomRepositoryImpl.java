package shop.kkeujeok.kkeujeokbackend.challenge.domain.repository;

import static shop.kkeujeok.kkeujeokbackend.challenge.domain.QChallenge.challenge;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSearchReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;

@Repository
@Transactional(readOnly = true)
public class ChallengeCustomRepositoryImpl implements ChallengeCustomRepository {
    private final JPAQueryFactory queryFactory;

    public ChallengeCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(challenges, pageable, total);
    }

    @Override
    public Page<Challenge> findChallengesByKeyWord(ChallengeSearchReqDto challengeSearchReqDto, Pageable pageable) {
        long total = Optional.ofNullable(
                queryFactory
                        .select(challenge.count())
                        .from(challenge)
                        .where(challenge.status.eq(Status.ACTIVE),
                                challenge.title.containsIgnoreCase(challengeSearchReqDto.keyWord()))
                        .fetchOne()
        ).orElse(0L);

        List<Challenge> challenges = queryFactory
                .selectFrom(challenge)
                .where(challenge.status.eq(Status.ACTIVE),
                        challenge.title.containsIgnoreCase(challengeSearchReqDto.keyWord()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(challenges, pageable, total);
    }
}
