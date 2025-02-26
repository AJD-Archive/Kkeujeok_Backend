package shop.kkeujeok.kkeujeokbackend.notification.util;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.kkeujeok.kkeujeokbackend.notification.domain.repository.EmitterRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseEmitterManager {

    private static final String DUMMY_MESSAGE = "연결 성공";
    private static final String SERVER_ID = UUID.randomUUID().toString();

    @Value("${sse.timeout}")
    private String defaultTimeout;

    private final RedisTemplate<String, String> redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final EmitterRepository emitterRepository;

    public SseEmitter createEmitter(Long memberId) {
        String emitterId = String.valueOf(memberId);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(Long.parseLong(defaultTimeout)));

        redisTemplate.opsForHash().put("sse_connections", emitterId, SERVER_ID);

        registerEmitterCallbacks(emitter, emitterId);

        sendNotification(emitter, emitterId, DUMMY_MESSAGE);

        return emitter;
    }

    private void registerEmitterCallbacks(SseEmitter emitter, String emitterId) {
        emitter.onCompletion(() -> {
            log.info("비동기 요청 완료");
            removeEmitter(emitterId);
        });

        emitter.onTimeout(() -> {
            log.info("시간 초과");
            emitter.complete();
            removeEmitter(emitterId);
        });

        emitter.onError((e) -> {
            log.error("Emitter 에러 발생", e);
            removeEmitter(emitterId);
        });
    }

    public void send(Long targetMemberId, String message) {
        String emitterId = String.valueOf(targetMemberId);
        String serverId = (String) redisTemplate.opsForHash().get("sse_connections", emitterId);

        if (serverId != null) {
            String data = emitterId + "|" + message;
            kafkaTemplate.send("sse_notifications", serverId, data);
        }
    }

    public void sendNotification(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .data(data));
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
    }

    public void removeEmitter(String emitterId) {
        emitterRepository.deleteById(emitterId);
        redisTemplate.opsForHash().delete("sse_connections", emitterId);
    }

}
