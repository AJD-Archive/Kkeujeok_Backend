package shop.kkeujeok.kkeujeokbackend.block.application.util;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.repository.BlockRepository;

@Component
@RequiredArgsConstructor
public class BlockCleanupScheduler {

    private final BlockRepository blockRepository;

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void deleteOldDeletedBlocks() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Block> blocksToDeletePermanently = blockRepository.findBlocksToDeletePermanently(thirtyDaysAgo);
        blockRepository.deleteAll(blocksToDeletePermanently);
    }

}
