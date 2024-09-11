package shop.kkeujeok.kkeujeokbackend.dashboard.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.AccessDeniedGroupException;

public class UnauthorizedAccessException extends AccessDeniedGroupException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException() {
        this("대시보드에 접근할 수 있는 권한이 없습니다");
    }
}
