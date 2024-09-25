package shop.kkeujeok.kkeujeokbackend.dashboard.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.InvalidGroupException;

public class DashboardDeletedException extends InvalidGroupException {
    public DashboardDeletedException(String message) {
        super(message);
    }

    public DashboardDeletedException() {
        this("삭제된 대시보드입니다.");
    }
}
