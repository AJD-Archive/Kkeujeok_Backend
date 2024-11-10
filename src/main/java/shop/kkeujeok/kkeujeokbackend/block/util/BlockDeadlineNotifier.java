package shop.kkeujeok.kkeujeokbackend.block.util;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.exception.MemberNotFoundException;
import shop.kkeujeok.kkeujeokbackend.notification.application.NotificationService;

@Component
@RequiredArgsConstructor
public class BlockDeadlineNotifier {
    private static final String DAILY_CRON_EXPRESSION = "0 0 0 * * ?"; // 매일 자정
    private static final String BLOCK_DEADLINE_MESSAGE_TEMPLATE = "블록 마감기한: %s 블록 마감까지 하루 남았습니다.";
    private static final String REDIS_KEY_PATTERN = "block:deadline:*";
    private static final String REDIS_VALUE_DELIMITER = ":";
    private static final int REDIS_VALUE_PARTS_MINIMUM_LENGTH = 3;
    private static final int MEMBER_EMAIL_INDEX = 1;
    private static final int BLOCK_TITLE_INDEX = 2;

    private final RedisTemplate<String, String> redisTemplate;
    private final NotificationService notificationService;
    private final MemberRepository memberRepository;

    @Scheduled(cron = DAILY_CRON_EXPRESSION)
    public void sendDeadlineNotifications() {
        Set<String> keys = getDeadlineKeys();
        for (String key : keys) {
            processKeyAndSendNotification(key);
        }
    }

    private Set<String> getDeadlineKeys() {
        Set<String> keys = redisTemplate.keys(REDIS_KEY_PATTERN);
        return keys != null ? keys : Set.of();
    }

    private void processKeyAndSendNotification(String key) {
        String deadlineValue = redisTemplate.opsForValue().get(key);
        if (deadlineValue == null) {
            return;
        }

        String[] parts = deadlineValue.split(REDIS_VALUE_DELIMITER);
        if (parts.length < REDIS_VALUE_PARTS_MINIMUM_LENGTH) {
            return;
        }

        String memberEmail = parts[MEMBER_EMAIL_INDEX];
        String blockTitle = parts[BLOCK_TITLE_INDEX];
        Member member = getMemberByEmail(memberEmail);
        String message = createDeadlineMessage(blockTitle);

        notificationService.sendNotification(member, message);
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    private String createDeadlineMessage(String blockTitle) {
        return String.format(BLOCK_DEADLINE_MESSAGE_TEMPLATE, blockTitle);
    }
}
