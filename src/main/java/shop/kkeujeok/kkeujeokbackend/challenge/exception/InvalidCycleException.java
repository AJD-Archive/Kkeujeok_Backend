package shop.kkeujeok.kkeujeokbackend.challenge.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.InvalidGroupException;

public class InvalidCycleException extends InvalidGroupException {
    private static final String DUPLICATE_DETAIL_MESSAGE = "중복된 주기 세부 항목이 발견되었습니다: %s %s";
    private static final String MISMATCH_DETAIL_MESSAGE = "주기와 일치하지 않는 세부 항목이 있습니다: %s %s";

    public InvalidCycleException(String message) {
        super(message);
    }

    public static InvalidCycleException forDuplicateDetail() {
        return new InvalidCycleException(DUPLICATE_DETAIL_MESSAGE);
    }

    public static InvalidCycleException forMismatchDetail() {
        return new InvalidCycleException(MISMATCH_DETAIL_MESSAGE);
    }
}
