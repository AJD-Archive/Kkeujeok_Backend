package shop.kkeujeok.kkeujeokbackend.challenge.application.util;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Type;
import shop.kkeujeok.kkeujeokbackend.block.domain.repository.BlockRepository;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;

@Component
@RequiredArgsConstructor
public class ChallengeBlockStatusUpdateScheduler {

    private final BlockRepository blockRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateStatuses() {
        List<Block> blocks = blockRepository.findByType(Type.CHALLENGE);

        blocks.forEach(block -> {
            if (!ChallengeBlockStatusUtil.isChallengeBlockActiveToday(block.getChallenge().getCycle(),
                    block.getChallenge().getCycleDetails())) {
                block.updateChallengeStatus(Status.UN_ACTIVE);
            }
            block.updateChallengeStatus(Status.ACTIVE);
        });
    }
}