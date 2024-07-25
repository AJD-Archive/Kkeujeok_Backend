package shop.kkeujeok.kkeujeokbackend.block.domain.repository;

import static shop.kkeujeok.kkeujeokbackend.block.domain.QBlock.block;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;

@Repository
@Transactional(readOnly = true)
public class BlockCustomRepositoryImpl implements BlockCustomRepository {
    private final JPAQueryFactory queryFactory;

    public BlockCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Block> findByBlockWithProgress(Progress progress, Pageable pageable) {
        long total = queryFactory
                .selectFrom(block)
                .where(block.progress.eq(progress))
                .stream()
                .count();

        List<Block> blocks = queryFactory
                .selectFrom(block)
                .where(block.progress.eq(progress))
                .orderBy(block.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(blocks, pageable, total);
    }

}
