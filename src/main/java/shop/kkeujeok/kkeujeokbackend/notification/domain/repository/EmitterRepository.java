package shop.kkeujeok.kkeujeokbackend.notification.domain.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public interface EmitterRepository {

    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    void deleteById(String emitterId);

    SseEmitter findById(String emitterId);

}
