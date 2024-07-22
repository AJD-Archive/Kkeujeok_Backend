package shop.kkeujeok.kkeujeokbackend.block.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.InvalidGroupException;

public class InvalidProgressException extends InvalidGroupException {
    public InvalidProgressException(String message) {
        super(message);
    }

    public InvalidProgressException() {
        this("유효하지 않은 상태입니다.");
    }
}
