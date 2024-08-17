package shop.kkeujeok.kkeujeokbackend.challenge.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.NotFoundGroupException;

public class CycleDetailsNotFoundException extends NotFoundGroupException {
    public CycleDetailsNotFoundException(String message) {
        super(message);
    }

    public CycleDetailsNotFoundException() {
        this("주기 세부정보가 존재하지 않습니다.");
    }
}
