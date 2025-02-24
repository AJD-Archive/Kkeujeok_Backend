package shop.kkeujeok.kkeujeokbackend.notification.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.kkeujeok.kkeujeokbackend.notification.domain.repository.EmitterRepository;
import shop.kkeujeok.kkeujeokbackend.notification.util.SseEmitterManager;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final SseEmitterManager sseEmitterManager;
    private final EmitterRepository emitterRepository;

    @KafkaListener(topics = "sse_notifications", groupId = "sse-group")
    public void listen(String message) {
        String[] parts = message.split("\\|");

        if (parts.length < 2) {
            return;
        }

        String emitterId = parts[0];
        String content = parts[1];

        SseEmitter emitter = emitterRepository.findById(emitterId);

        if (emitter != null) {
            sseEmitterManager.sendNotification(emitter, emitterId, content);
        }
    }

}
