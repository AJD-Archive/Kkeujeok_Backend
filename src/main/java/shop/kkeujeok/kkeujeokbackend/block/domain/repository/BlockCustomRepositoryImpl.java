package shop.kkeujeok.kkeujeokbackend.block.domain.repository;

import static shop.kkeujeok.kkeujeokbackend.block.domain.QBlock.block;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Repository
@Transactional(readOnly = true)
public class BlockCustomRepositoryImpl implements BlockCustomRepository {
    private final JPAQueryFactory queryFactory;

    public BlockCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

//    @Override
//    public Page<Block> findByBlockWithProgress(Long dashboardId, Progress progress, Pageable pageable) {
//        long total = queryFactory
//                .selectFrom(block)
//                .where(block.progress.eq(progress)
//                        .and(block.dashboard.id.eq(dashboardId))
//                        .and(block.status.eq(Status.ACTIVE)))
//                .stream()
//                .count();
//
//        List<Block> blocks = queryFactory
//                .selectFrom(block)
//                .where(block.dashboard.id.eq(dashboardId)
//                        .and(block.progress.eq(progress))
//                        .and(block.status.eq(Status.ACTIVE)))
//                .orderBy(block.sequence.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        return new PageImpl<>(blocks, pageable, total);
//    }

    @Override
    public Page<BlockInfoResDto> findForBlockByProgress(Long dashboardId, Progress progress,
                                                        Pageable pageable) {
        List<BlockInfoResDto> blockInfoResDtoList = queryFactory
                .select(Projections.constructor(BlockInfoResDto.class,
                        block.id,
                        block.title,
                        block.contents,
                        block.progress,
                        block.type,
                        block.dashboard.dType,
                        block.startDate,
                        block.deadLine,
                        block.member.nickname,
                        block.member.picture,
                        block.deadLine)
                )
                .from(block)
                .where(block.dashboard.id.eq(dashboardId)
                        .and(block.progress.eq(progress))
                        .and(block.status.eq(Status.ACTIVE)))
                .orderBy(block.sequence.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BlockInfoResDto> result = blockInfoResDtoList.stream()
                .map(blockInfo -> {
                    String dDay = calculateDDay(blockInfo.deadLine());
                    return new BlockInfoResDto(
                            blockInfo.blockId(),
                            blockInfo.title(),
                            blockInfo.contents(),
                            blockInfo.progress(),
                            blockInfo.type(),
                            blockInfo.dType(),
                            blockInfo.startDate(),
                            blockInfo.deadLine(),
                            blockInfo.nickname(),
                            blockInfo.picture(),
                            dDay
                    );
                })
                .toList();

        long total = queryFactory
                .select(block.id.count())
                .from(block)
                .where(block.dashboard.id.eq(dashboardId)
                        .and(block.progress.eq(progress))
                        .and(block.status.eq(Status.ACTIVE)))
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }

    private static String calculateDDay(String deadlineStr) {
        LocalDate deadlineDate = LocalDateTime.parse(deadlineStr, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))
                .toLocalDate();
        LocalDate today = LocalDate.now();

        long daysBetween = ChronoUnit.DAYS.between(today, deadlineDate);

        if (today.isBefore(deadlineDate)) {
            return "D-" + daysBetween;
        } else if (today.isEqual(deadlineDate)) {
            return "D-Day";
        } else {
            return "D+" + Math.abs(daysBetween);
        }
    }

    @Override
    public int findLastSequenceByProgress(Member member, Long dashboardId, Progress progress) {
        return Optional.of(
                        Math.toIntExact(
                                queryFactory
                                        .select(block.sequence)
                                        .from(block)
                                        .where(block.dashboard.id.eq(dashboardId)
                                                .and(block.progress.eq(progress))
                                                .and(block.status.eq(Status.ACTIVE)))
                                        .stream()
                                        .count()
                        )
                )
                .orElse(0);
    }

    @Override
    public Page<Block> findByDeletedBlocks(Long dashboardId, Pageable pageable) {
        long total = queryFactory
                .selectFrom(block)
                .where(block.dashboard.id.eq(dashboardId)
                        .and(block.status.eq(Status.DELETED)))
                .stream()
                .count();

        List<Block> blocks = queryFactory
                .selectFrom(block)
                .where(block.dashboard.id.eq(dashboardId)
                        .and(block.status.eq(Status.DELETED)))
                .orderBy(block.sequence.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(blocks, pageable, total);
    }

    @Override
    public List<Block> findByDeletedBlocks(Long dashboardId) {
        return queryFactory
                .selectFrom(block)
                .where(block.dashboard.id.eq(dashboardId)
                        .and(block.status.eq(Status.DELETED)))
                .orderBy(block.sequence.desc())
                .fetch();
    }

    @Override
    public List<Block> findBlocksToDeletePermanently(LocalDateTime thirtyDaysAgo) {
        return queryFactory.selectFrom(block)
                .where(block.status.eq(Status.DELETED)
                        .and(block.updatedAt.before(thirtyDaysAgo)))
                .fetch();
    }

}
