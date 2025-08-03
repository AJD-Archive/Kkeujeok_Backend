package shop.kkeujeok.kkeujeokbackend.challenge.domain.repository;

import static shop.kkeujeok.kkeujeokbackend.challenge.domain.QChallenge.challenge;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSearchReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeInfoResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Category;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.QChallenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.QChallengeMemberMapping;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.QMember;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeCustomRepositoryImpl implements ChallengeCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChallengeInfoResDto> findAllChallenges(Pageable pageable) {

        QChallenge challenge = QChallenge.challenge;
        QChallengeMemberMapping mapping = QChallengeMemberMapping.challengeMemberMapping;
        QMember member = QMember.member;

        long total = Optional.ofNullable(
                queryFactory
                        .select(challenge.count())
                        .from(challenge)
                        .where(challenge.status.eq(Status.ACTIVE))
                        .fetchOne()
        ).orElse(0L);

        List<ChallengeInfoResDto> challenges = queryFactory
                .select(Projections.constructor(
                        ChallengeInfoResDto.class,
                        challenge.id,
                        member.id,
                        challenge.title,
                        challenge.contents,
                        challenge.category,
                        challenge.cycle,
                        challenge.cycleDetails,
                        challenge.startDate,
                        challenge.endDate,
                        challenge.representImage,
                        member.nickname,
                        member.picture,
                        challenge.blockName,
                        challenge.participants.size(),
                        Expressions.constant(false), // isParticipant
                        Expressions.constant(false), // isAuthor
                        Expressions.constant(Collections.emptySet()), // completedMembers
                        challenge.createdAt

                ))
                .from(challenge)
                .leftJoin(challenge.participants, mapping)
                .leftJoin(mapping.member, member)
                .where(challenge.status.eq(Status.ACTIVE))
                .orderBy(challenge.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        return new PageImpl<>(challenges, pageable, total);
    }

    @Override
    public Page<ChallengeInfoResDto> findChallengesByMemberInMapping(Member member, Pageable pageable) {
        QChallenge challenge = QChallenge.challenge;
        QChallengeMemberMapping mapping = QChallengeMemberMapping.challengeMemberMapping;
        QMember m = QMember.member;

        long total = Optional.ofNullable(
                queryFactory
                        .select(challenge.count())
                        .from(challenge)
                        .where(challenge.status.eq(Status.ACTIVE))
                        .fetchOne()
        ).orElse(0L);

        List<ChallengeInfoResDto> results = queryFactory
                .select(Projections.constructor(
                        ChallengeInfoResDto.class,
                        challenge.id,
                        m.id,
                        challenge.title,
                        challenge.contents,
                        challenge.category,
                        challenge.cycle,
                        challenge.cycleDetails,
                        challenge.startDate,
                        challenge.endDate,
                        challenge.representImage,
                        m.nickname,
                        m.picture,
                        challenge.blockName,
                        challenge.participants.size(),
                        Expressions.constant(true),
                        Expressions.constant(false),
                        Expressions.constant(Collections.emptySet()),
                        challenge.createdAt
                ))
                .from(mapping)
                .join(mapping.challenge, challenge)
                .join(challenge.member, m)
                .where(mapping.member.eq(member)
                        .and(challenge.status.eq(Status.ACTIVE)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<ChallengeInfoResDto> findChallengesByCategoryAndKeyword(ChallengeSearchReqDto challengeSearchReqDto,
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

        List<ChallengeInfoResDto> results = queryFactory
                .select(Projections.constructor(
                        ChallengeInfoResDto.class,
                        challenge.id,
                        challenge.member.id,
                        challenge.title,
                        challenge.contents,
                        challenge.category,
                        challenge.cycle,
                        challenge.cycleDetails,
                        challenge.startDate,
                        challenge.endDate,
                        challenge.representImage,
                        challenge.member.nickname,
                        challenge.member.picture,
                        challenge.blockName,
                        challenge.participants.size(),
                        Expressions.constant(false), // isParticipant
                        Expressions.constant(false), // isAuthor
                        Expressions.constant(Collections.emptySet()),
                        challenge.createdAt
                ))
                .from(challenge)
                .where(predicate)
                .orderBy(challenge.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }
}
