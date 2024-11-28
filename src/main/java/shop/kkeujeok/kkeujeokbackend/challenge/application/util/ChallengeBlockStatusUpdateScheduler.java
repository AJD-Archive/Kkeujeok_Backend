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
    private static final String CHALLENGE_WITHDRAW_MESSAGE_TEMPLATE = "%s 챌린지가 탈퇴되었습니다.";

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

        activeMappings.stream()
                .filter(this::isNewBlockNeeded)
                .forEach(this::processMappingForNewBlock);
    }

    private boolean isNewBlockNeeded(ChallengeMemberMapping mapping) {
        return mapping.getChallenge().isActiveToday();
    }

    private void processMappingForNewBlock(ChallengeMemberMapping mapping) {
        if (shouldRemoveParticipant(mapping)) {
            removeParticipantFromChallenge(mapping);
            return;
        }
        createAndSaveBlock(mapping);
        notifyMemberForNewBlock(mapping);
    }

    private boolean shouldRemoveParticipant(ChallengeMemberMapping mapping) {
        PersonalDashboard dashboard = mapping.getPersonalDashboard();
        Challenge challenge = mapping.getChallenge();

        long existingChallengeBlockCount = dashboard.getBlocks().stream()
                .filter(block -> block.getProgress().equals(Progress.NOT_STARTED))
                .filter(block -> block.getType().equals(Type.CHALLENGE))
                .filter(block -> block.getChallenge().getId().equals(challenge.getId()))
                .count();

        return existingChallengeBlockCount >= 5;
    }

    private void removeParticipantFromChallenge(ChallengeMemberMapping mapping) {
        Challenge challenge = mapping.getChallenge();
        challenge.removeParticipant(mapping);
        notifyMemberForWithdrawChallenge(mapping, challenge);
    }

    private void notifyMemberForWithdrawChallenge(ChallengeMemberMapping mapping, Challenge challenge) {
        Member member = mapping.getMember();
        String message = String.format(CHALLENGE_WITHDRAW_MESSAGE_TEMPLATE, challenge.getTitle());
        notificationService.sendNotification(member, message);
    }

    private void createAndSaveBlock(ChallengeMemberMapping mapping) {
        Block newBlock = Block.builder()
                .title(mapping.getChallenge().getTitle())
                .contents(mapping.getChallenge().getContents())
                .progress(Progress.NOT_STARTED)
                .type(Type.CHALLENGE)
                .deadLine(getFormattedDeadline())
                .member(mapping.getMember())
                .dashboard(mapping.getPersonalDashboard())
                .challenge(mapping.getChallenge())
                .build();

        blockRepository.save(newBlock);
    }

    private String getFormattedDeadline() {
        return LocalDate.now()
                .plusDays(DEADLINE_EXTENSION_DAYS)
                .format(DateTimeFormatter.ofPattern(DEADLINE_DATE_FORMAT));
    }

    private void notifyMemberForNewBlock(ChallengeMemberMapping mapping) {
        Member member = mapping.getMember();
        Challenge challenge = mapping.getChallenge();
        String message = String.format(CHALLENGE_CREATED_MESSAGE_TEMPLATE, challenge.getTitle());
        notificationService.sendNotification(member, message);
    }
}
