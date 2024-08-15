package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.repository;

import static shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.QFile.file;

import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.File;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;

@Repository
@Transactional(readOnly = true)
public class FileCustomRepositoryImpl implements FileCustomRepository {

    private final JPAQueryFactory queryFactory;

    public FileCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<File> findByFilesWithDocumentId(Long documentId, Pageable pageable) {
        long total = queryFactory
                .selectFrom(file)
                .where(file.document.id.eq(documentId)
                        .and(file.status.eq(Status.ACTIVE)))
                .fetchCount();

        List<File> files = queryFactory
                .selectFrom(file)
                .where(file.document.id.eq(documentId)
                        .and(file.status.eq(Status.ACTIVE)))
                .orderBy(file.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(files, pageable, total);
    }
}
