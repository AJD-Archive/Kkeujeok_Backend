package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.repository;

import static shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.QDocument.document;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;

@Repository
@Transactional(readOnly = true)
public class DocumentCustomRepositoryImpl implements DocumentCustomRepository {

    private final JPAQueryFactory queryFactory;

    public DocumentCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Document> findByDocumentWithTeamDashboard(Long teamDashboardId, Pageable pageable) {
        long total = queryFactory
                .selectFrom(document)
                .where(document.teamDashboard.id.eq(teamDashboardId)
                        .and(document.status.eq(Status.ACTIVE)))
                .fetchCount();

        List<Document> documents = queryFactory
                .selectFrom(document)
                .where(document.teamDashboard.id.eq(teamDashboardId)
                        .and(document.status.eq(Status.ACTIVE)))
                .orderBy(document.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(documents, pageable, total);
    }

}
