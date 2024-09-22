package shop.kkeujeok.kkeujeokbackend.challenge.application.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.Type;
import shop.kkeujeok.kkeujeokbackend.block.domain.repository.BlockRepository;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.ChallengeMemberMapping;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.repository.challengeMemberMapping.ChallengeMemberMappingRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.notification.application.NotificationService;

@Component
@RequiredArgsConstructor
public class ChallengeBlockStatusUpdateScheduler {

    private static final String DAILY_CRON_EXPRESSION = "0 0 0 * * ?";
    private static final String CHALLENGE_CREATED_MESSAGE_TEMPLATE = "%s 챌린지 블록이 생성되었습니다.";
    private static final String DEADLINE_DATE_FORMAT = "yyyy.MM.dd 23:59";
    private static final int DEADLINE_EXTENSION_DAYS = 1;

    private final ChallengeMemberMappingRepository challengeMemberMappingRepository;
    private final BlockRepository blockRepository;
    private final NotificationService notificationService;

    @Transactional
    @Scheduled(cron = DAILY_CRON_EXPRESSION)
    public void createNewChallengeBlocks() {
        List<ChallengeMemberMapping> activeMappings = challengeMemberMappingRepository.findActiveMappings();

        for (ChallengeMemberMapping mapping : activeMappings) {
            if (isNewBlockNeeded(mapping)) {
                createNewBlock(mapping);
            }
        }
    }

    private boolean isNewBlockNeeded(ChallengeMemberMapping mapping) {
        return mapping.getChallenge().isActiveToday();
    }

    private void createNewBlock(ChallengeMemberMapping mapping) {
        PersonalDashboard dashboard = mapping.getPersonalDashboard();

        // 새로운 블록 생성
        Block newBlock = Block.builder()
                .title(mapping.getChallenge().getTitle())
                .contents(mapping.getChallenge().getContents())
                .progress(Progress.NOT_STARTED)
                .type(Type.CHALLENGE)
                .deadLine(LocalDate.now().plusDays(DEADLINE_EXTENSION_DAYS)
                        .format(DateTimeFormatter.ofPattern(DEADLINE_DATE_FORMAT)))
                .member(mapping.getMember())
                .dashboard(dashboard)
                .challenge(mapping.getChallenge())
                .build();

        // 블록 저장
        blockRepository.save(newBlock);

        // 알림 전송
        sendChallengeCreatedNotification(mapping.getMember(), mapping.getChallenge());
    }

    // 챌린지 생성 알림 전송
    private void sendChallengeCreatedNotification(Member member, Challenge challenge) {
        String message = String.format(CHALLENGE_CREATED_MESSAGE_TEMPLATE, challenge.getTitle());
        notificationService.sendNotification(member, message);
    }
}
