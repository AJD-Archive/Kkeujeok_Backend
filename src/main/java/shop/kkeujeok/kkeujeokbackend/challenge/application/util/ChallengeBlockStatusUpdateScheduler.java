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

    private static final String DAILY_CRON_EXPRESSION = "0 0 0 * * ?";
    private static final String CHALLENGE_CREATED_MESSAGE_TEMPLATE = "%s 챌린지가 생성되었습니다";

    private final BlockRepository blockRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = DAILY_CRON_EXPRESSION)
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
                String message = String.format(CHALLENGE_CREATED_MESSAGE_TEMPLATE, block.getChallenge().getTitle());

                notificationService.sendNotification(member, message);
            }
        });
    }
}
