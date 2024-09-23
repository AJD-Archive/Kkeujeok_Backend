package shop.kkeujeok.kkeujeokbackend.dashboard.domain.repository;

import static shop.kkeujeok.kkeujeokbackend.block.domain.QBlock.block;
import static shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.QPersonalDashboard.personalDashboard;
import static shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.QTeamDashboard.teamDashboard;
import static shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.QTeamDashboardMemberMapping.teamDashboardMemberMapping;
import static shop.kkeujeok.kkeujeokbackend.member.domain.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboardMemberMapping;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Repository
@Transactional(readOnly = true)
public class DashboardCustomRepositoryImpl implements DashboardCustomRepository {

    private final JPAQueryFactory queryFactory;

    public DashboardCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<PersonalDashboard> findForPersonalDashboard(Member member) {
        return queryFactory
                .selectFrom(personalDashboard)
                .where(personalDashboard._super.member.eq(member)
                        .and(personalDashboard._super.status.eq(Status.ACTIVE)))
                .fetch();
    }

    @Override
    public Page<PersonalDashboard> findForPersonalDashboard(Member member, Pageable pageable) {
        long total = queryFactory
                .selectFrom(personalDashboard)
                .where(personalDashboard._super.member.eq(member)
                        .and(personalDashboard._super.status.eq(Status.ACTIVE)))
                .fetchCount();

        List<PersonalDashboard> dashboards = queryFactory
                .selectFrom(personalDashboard)
                .where(personalDashboard._super.member.eq(member)
                        .and(personalDashboard._super.status.eq(Status.ACTIVE)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(dashboards, pageable, total);
    }

    @Override
    public Set<String> findCategoriesForDashboard(Member member) {
        return queryFactory
                .select(personalDashboard.category)
                .from(personalDashboard)
                .where(personalDashboard._super.member.eq(member)
                        .and(personalDashboard._super.status.eq(Status.ACTIVE)))
                .stream()
                .collect(Collectors.toSet());
    }

    @Override
    public Page<TeamDashboard> findForTeamDashboard(Member member, Pageable pageable) {
        long total = queryFactory
                .selectFrom(teamDashboard)
                .where(teamDashboard._super.member.eq(member))
                .stream()
                .count();

        List<TeamDashboard> dashboards = queryFactory
                .selectFrom(teamDashboard)
                .where(teamDashboard._super.member.eq(member)
                        .and(teamDashboard._super.status.eq(Status.ACTIVE)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(dashboards, pageable, total);
    }

    @Override
    public List<TeamDashboard> findForTeamDashboard(Member member) {
        return queryFactory
                .select(teamDashboard)
                .from(teamDashboard)
                .leftJoin(teamDashboard.teamDashboardMemberMappings, teamDashboardMemberMapping)
                .where(
                        teamDashboardMemberMapping.member.eq(member)
                                .or(teamDashboard._super.member.eq(member))
                                .and(teamDashboard._super.status.eq(Status.ACTIVE))
                )
                .fetch();
    }

    @Override
    public List<Member> findForMembersByQuery(String query) {
        if (query.contains("#")) {
            String[] parts = query.split("#");
            String nickname = parts[0];
            String tag = "#" + parts[1];

            return queryFactory
                    .selectFrom(member)
                    .where(member.nickname.eq(nickname)
                            .and(member.tag.eq(tag)))
                    .fetch();
        }

        return queryFactory
                .selectFrom(member)
                .where(member.email.eq(query))
                .fetch();
    }

    @Override
    public double calculateCompletionPercentage(Long dashboardId) {
        List<Block> blocks = queryFactory
                .selectFrom(block)
                .where(block.dashboard.id.eq(dashboardId))
                .fetch();

        long totalBlocks = blocks.size();

        long completedBlocks = blocks.stream()
                .filter(b -> b.getProgress().equals(Progress.COMPLETED))
                .count();

        if (totalBlocks == 0) {
            return 0;
        }

        return (double) completedBlocks / totalBlocks * 100;
    }

    @Override
    public Page<PersonalDashboard> findPublicPersonalDashboard(Member member, Pageable pageable) {
        long total = queryFactory
                .selectFrom(personalDashboard)
                .where(personalDashboard._super.member.eq(member)
                        .and(personalDashboard._super.status.eq(Status.ACTIVE))
                        .and(personalDashboard.isPublic.eq(true))) // isPublic이 true인 경우
                .fetchCount();

        List<PersonalDashboard> dashboards = queryFactory
                .selectFrom(personalDashboard)
                .where(personalDashboard._super.member.eq(member)
                        .and(personalDashboard._super.status.eq(Status.ACTIVE))
                        .and(personalDashboard.isPublic.eq(true)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(dashboards, pageable, total);
    }
}
