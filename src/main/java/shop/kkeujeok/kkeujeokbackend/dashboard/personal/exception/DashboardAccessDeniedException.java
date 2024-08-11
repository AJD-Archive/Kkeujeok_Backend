package shop.kkeujeok.kkeujeokbackend.dashboard.personal.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.AccessDeniedGroupException;

public class DashboardAccessDeniedException extends AccessDeniedGroupException {
    public DashboardAccessDeniedException(String message) {
        super(message);
    }

    public DashboardAccessDeniedException() {
        this("대시보드 생성자가 아닙니다.");
    }
}
