package shop.kkeujeok.kkeujeokbackend.block.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

public interface BlockCustomRepository {
    Page<BlockInfoResDto> findForBlockByProgress(Long dashboardId, Progress progress, Pageable pageable);
//    Page<Block> findByBlockWithProgress(Long dashboardId, Progress progress, Pageable pageable);

    int findLastSequenceByProgress(Member member, Long dashboardId, Progress progress);

    Page<Block> findByDeletedBlocks(Long dashboardId, Pageable pageable);

    List<Block> findByDeletedBlocks(Long dashboardId);

    List<Block> findBlocksToDeletePermanently(LocalDateTime thirtyDaysAgo);
}
