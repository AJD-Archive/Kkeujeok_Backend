package shop.kkeujeok.kkeujeokbackend.member.follow.domain.repository;

import static shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.QTeamDashboard.teamDashboard;
import static shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.QTeamDashboardMemberMapping.teamDashboardMemberMapping;
import static shop.kkeujeok.kkeujeokbackend.member.domain.QMember.member;
import static shop.kkeujeok.kkeujeokbackend.member.follow.domain.QFollow.follow;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.FollowInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.MemberInfoForFollowResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.MyFollowsResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.RecommendedFollowInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.Follow;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.FollowStatus;
import shop.kkeujeok.kkeujeokbackend.member.follow.exception.FollowAlreadyAcceptException;

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
    public void acceptFollowingRequest(Long followId) {
        checkIfAlreadyAccepted(followId);

        new JPAUpdateClause(entityManager, follow)
                .where(follow.id.eq(followId))
                .set(follow.followStatus, FollowStatus.ACCEPT)
                .execute();
    }

    private void checkIfAlreadyAccepted(Long followId) {
        FollowStatus currentStatus = new JPAQuery<>(entityManager)
                .select(follow.followStatus)
                .from(follow)
                .where(follow.id.eq(followId))
                .fetchOne();

        if (currentStatus == FollowStatus.ACCEPT) {
            throw new FollowAlreadyAcceptException();
        }
    }

    @Override
    public Page<FollowInfoResDto> findFollowList(Long memberId, Pageable pageable) {
        List<FollowInfoResDto> fetch = queryFactory
                .selectFrom(follow)
                .where(follow.fromMember.id.eq(memberId)
                        .or(follow.toMember.id.eq(memberId))
                        .and(follow.followStatus.eq(FollowStatus.ACCEPT)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(follow -> FollowInfoResDto.of(follow, memberId))
                .collect(Collectors.toList());

        long total = Optional.ofNullable(queryFactory
                .select(follow.count())
                .from(follow)
                .where(follow.fromMember.id.eq(memberId)
                        .or(follow.toMember.id.eq(memberId))
                        .and(follow.followStatus.eq(FollowStatus.ACCEPT)))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(fetch, pageable, total);
    }

    @Override
    public Page<RecommendedFollowInfoResDto> findRecommendedFollowList(Long memberId, Pageable pageable) {
        List<Long> excludedIds = queryFactory
                .select(follow.fromMember.id)
                .from(follow)
                .where(
                        follow.toMember.id.eq(memberId) // 나를 팔로우한 사람
                                .or(follow.fromMember.id.eq(memberId)) // 내가 팔로우한 사람
                                .or(
                                        follow.fromMember.id.in( // fromMember와 toMember가 바뀐 경우
                                                queryFactory
                                                        .select(follow.toMember.id)
                                                        .from(follow)
                                                        .where(follow.fromMember.id.eq(memberId))
                                        )
                                )
                                .or(
                                        follow.toMember.id.in( // toMember와 fromMember가 바뀐 경우
                                                queryFactory
                                                        .select(follow.fromMember.id)
                                                        .from(follow)
                                                        .where(follow.toMember.id.eq(memberId))
                                        )
                                )
                )
                .fetch();

        // 잠재적 친구와 대시보드 소유자를 포함하여 추천 친구를 조회
        List<Member> potentialFriends = queryFactory
                .select(member)
                .distinct() // 중복 제거
                .from(teamDashboardMemberMapping)
                .join(teamDashboardMemberMapping.member, member)
                .join(teamDashboardMemberMapping.teamDashboard, teamDashboard)
                .where(
                        teamDashboard.id.in(
                                        queryFactory
                                                .select(teamDashboard.id)
                                                .from(teamDashboard)
                                                .where(
                                                        teamDashboard.member.id.eq(memberId) // 대시보드 소유자
                                                                .or(teamDashboardMemberMapping.member.id.eq(memberId))
                                                        // 대시보드 멤버
                                                )
                                )
                                .or(teamDashboardMemberMapping.teamDashboard.id.in( // 같은 teamDashboard에 속한 멤버
                                        queryFactory
                                                .select(teamDashboardMemberMapping.teamDashboard.id)
                                                .from(teamDashboardMemberMapping)
                                                .where(teamDashboardMemberMapping.member.id.eq(memberId))
                                ))
                )
                .where(member.id.ne(memberId)) // 본인 제외
                .where(member.id.notIn(excludedIds)) // 이미 팔로우된 멤버 제외
                .offset(pageable.getOffset()) // 페이징 시작 위치
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch();

        // 추천 친구 목록을 DTO로 변환
        List<RecommendedFollowInfoResDto> recommendedFollows = potentialFriends.stream()
                .map(teamMember -> RecommendedFollowInfoResDto.from(teamMember, false)) // DTO 변환
                .collect(Collectors.toList());

        // 결과 반환
        return new PageImpl<>(recommendedFollows, pageable, recommendedFollows.size());
    }

    @Override
    public Optional<Follow> findByFromMemberAndToMember(Member fromMember, Member toMember) {
        Follow followRecord = queryFactory
                .selectFrom(follow)
                .where(
                        (follow.fromMember.eq(fromMember).and(follow.toMember.eq(toMember)))
                                .or(follow.fromMember.eq(toMember).and(follow.toMember.eq(fromMember))) // 양방향 조회
                )
                .fetchOne();

        return Optional.ofNullable(followRecord);
    }

    @Override
    public Page<MemberInfoForFollowResDto> searchFollowListUsingKeywords(Long memberId, String keyword,
                                                                         Pageable pageable) {
        BooleanExpression keywordCondition = buildKeywordCondition(keyword);

        List<MemberInfoForFollowResDto> members = queryFactory
                .select(Projections.constructor(MemberInfoForFollowResDto.class,
                        member.id,
                        member.nickname,
                        member.name,
                        member.picture,
                        follow.followStatus.eq(FollowStatus.ACCEPT)
                ))
                .from(member)
                .leftJoin(follow)
                .on(
                        (follow.fromMember.id.eq(memberId).and(follow.toMember.id.eq(member.id)))
                                .or(follow.fromMember.id.eq(member.id).and(follow.toMember.id.eq(memberId)))
                )
                .where(
                        member.id.ne(memberId)
                                .and(keywordCondition)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(queryFactory
                .select(member.count())
                .from(member)
                .where(
                        member.id.ne(memberId)
                                .and(keywordCondition)
                )
                .fetchOne()).orElse(0L);

        return new PageImpl<>(members, pageable, total);
    }

    private BooleanExpression buildKeywordCondition(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return member.name.containsIgnoreCase(keyword).or(member.email.containsIgnoreCase(keyword));
    }

    @Override
    public MyFollowsResDto findMyFollowsCount(Long memberId) {
        int followCount = (int) queryFactory
                .select(follow)
                .from(follow)
                .where(
                        (follow.fromMember.id.eq(memberId)
                                .or(follow.toMember.id.eq(memberId)))
                                .and(follow.followStatus.eq(FollowStatus.ACCEPT))
                )
                .fetchCount();

        return MyFollowsResDto.from(followCount);
    }

    @Override
    public boolean existsAlreadyFollow(Long followId) {
        return queryFactory
                .selectOne()
                .from(follow)
                .where(follow.id.eq(followId)
                        .and(follow.followStatus.eq(FollowStatus.ACCEPT)))
                .fetchFirst() != null;
    }
}
