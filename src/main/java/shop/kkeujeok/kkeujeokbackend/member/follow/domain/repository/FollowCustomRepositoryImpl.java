package shop.kkeujeok.kkeujeokbackend.member.follow.domain.repository;

import static shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.QTeamDashboard.teamDashboard;
import static shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.QTeamDashboardMemberMapping.teamDashboardMemberMapping;
import static shop.kkeujeok.kkeujeokbackend.member.domain.QMember.member;
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
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.RecommendedFollowInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.Follow;
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
                .map(follow -> FollowInfoResDto.of(follow, memberId))
                .collect(Collectors.toList());

        long total = Optional.ofNullable(queryFactory
                .select(follow.count())
                .from(follow)
                .where(follow.fromMember.id.eq(memberId)
                        .or(follow.toMember.id.eq(memberId)))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(fetch, pageable, total);
    }

    @Override
    public Page<RecommendedFollowInfoResDto> findRecommendedFollowList(Long memberId, Pageable pageable) {
        List<Member> potentialFriends = queryFactory
                .select(member)
                .from(teamDashboardMemberMapping)
                .join(teamDashboardMemberMapping.member, member)
                .join(teamDashboardMemberMapping.teamDashboard, teamDashboard)
                .where(
                        teamDashboard.id.in(
                                queryFactory
                                        .select(teamDashboard.id)
                                        .from(teamDashboard)
                                        .where(
                                                teamDashboard.member.id.eq(memberId)
                                                        .or(teamDashboardMemberMapping.member.id.eq(memberId))
                                        )
                        )
                )
                .where(member.id.ne(memberId))
                .fetch();

        List<Member> dashboardOwners = queryFactory
                .select(teamDashboard.member)
                .from(teamDashboard)
                .where(teamDashboard.id.in(
                        queryFactory
                                .select(teamDashboard.id)
                                .from(teamDashboardMemberMapping)
                                .where(teamDashboardMemberMapping.member.id.eq(memberId))
                ))
                .fetch();

        potentialFriends.addAll(dashboardOwners);

        potentialFriends = potentialFriends.stream().distinct().collect(Collectors.toList());

        List<RecommendedFollowInfoResDto> recommendedFollows = potentialFriends.stream()
                .filter(teamMember -> !teamMember.getId().equals(memberId)) // 본인 제외
                .filter(teamMember -> !existsByFromMemberAndToMember(
                        entityManager.find(Member.class, memberId), teamMember)) // 친구 관계가 없는 멤버만 포함
                .map(RecommendedFollowInfoResDto::from)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), recommendedFollows.size());
        List<RecommendedFollowInfoResDto> pagedRecommendedFollows = recommendedFollows.subList(start, end);

        return new PageImpl<>(pagedRecommendedFollows, pageable, recommendedFollows.size());
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


}
