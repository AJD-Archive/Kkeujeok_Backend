package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.domain.TeamDocument;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.domain.QTeamDocument.teamDocument;

@Repository
@Transactional(readOnly = true)
public class TeamDocumentCustomRepositoryImpl implements TeamDocumentCustomRepository {

    private final JPAQueryFactory queryFactory;

    public TeamDocumentCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<TeamDocument> findForTeamDocumentByCategory(TeamDashboard teamDashboard, String category, Pageable pageable) {
        List<TeamDocument> results = queryFactory
                .selectFrom(teamDocument)
                .where(
                        teamDocument.TeamDashboard.eq(teamDashboard),
                        categoryEq(category),
                        teamDocument.status.eq(Status.ACTIVE)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(teamDocument)
                .where(
                        teamDocument.TeamDashboard.eq(teamDashboard),
                        categoryEq(category),
                        teamDocument.status.eq(Status.ACTIVE)
                )
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression categoryEq(String category) {
        return (category == null || category.trim().isEmpty()) ? null : teamDocument.category.eq(category);
    }

    @Override
    public List<String> findTeamDocumentCategory(TeamDashboard teamDashboard) {
        Set<String> uniqueCategories = queryFactory
                .select(teamDocument.category)
                .from(teamDocument)
                .where(teamDocument.TeamDashboard.eq(teamDashboard)
                        ,teamDocument.status.eq(Status.ACTIVE))
                .stream()
                .collect(Collectors.toSet());

        return uniqueCategories.stream().toList();
    }
}
