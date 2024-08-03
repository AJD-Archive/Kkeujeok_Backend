package shop.kkeujeok.kkeujeokbackend.challenge.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.InvalidGroupException;

public class InvalidCycleException extends InvalidGroupException {
    public InvalidCycleException(String message) {
        super(message);
    }
}
