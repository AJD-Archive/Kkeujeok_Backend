package shop.kkeujeok.kkeujeokbackend.notification.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.NotFoundGroupException;

public class NotificationNotFoundException extends NotFoundGroupException {
    public NotificationNotFoundException(String message) {
        super(message);
    }

    public NotificationNotFoundException() {
        this("존재하지 않는 알림입니다.");
    }
}
