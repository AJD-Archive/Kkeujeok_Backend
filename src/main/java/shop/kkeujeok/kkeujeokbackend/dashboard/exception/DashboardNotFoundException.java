package shop.kkeujeok.kkeujeokbackend.dashboard.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.NotFoundGroupException;

public class DashboardNotFoundException extends NotFoundGroupException {

    public DashboardNotFoundException(String message) {
        super(message);
    }

    public DashboardNotFoundException() {
        this("존재하지 않는 대시보드 입니다.");
    }
}
