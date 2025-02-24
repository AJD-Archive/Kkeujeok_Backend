package shop.kkeujeok.kkeujeokbackend.notification.domain.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void deleteById(String emitterId) {
        emitters.remove(emitterId);
    }

    @Override
    public SseEmitter findById(String emitterId) {
        return emitters.get(emitterId);
    }

}
