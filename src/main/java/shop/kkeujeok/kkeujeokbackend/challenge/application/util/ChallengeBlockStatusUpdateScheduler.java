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
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.notification.application.NotificationService;

@Component
@RequiredArgsConstructor
public class ChallengeBlockStatusUpdateScheduler {

    private final BlockRepository blockRepository;
    private final NotificationService notificationService; // NotificationService 주입

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateStatuses() {
        List<Block> blocks = blockRepository.findByType(Type.CHALLENGE);

        blocks.forEach(block -> {
            Status previousStatus = block.getStatus();
            Status newStatus;

            if (!ChallengeBlockStatusUtil.isChallengeBlockActiveToday(block.getChallenge().getCycle(),
                    block.getChallenge().getCycleDetails())) {
                newStatus = Status.UN_ACTIVE;
            } else {
                newStatus = Status.ACTIVE;
            }

            if (newStatus == Status.ACTIVE && previousStatus != Status.ACTIVE) {
                block.updateChallengeStatus(newStatus);

                Member member = block.getMember();
                String message = String.format("%s 챌린지가 생성되었습니다", block.getChallenge().getTitle());

                notificationService.sendNotification(member, message);
            }
        });
    }
}
