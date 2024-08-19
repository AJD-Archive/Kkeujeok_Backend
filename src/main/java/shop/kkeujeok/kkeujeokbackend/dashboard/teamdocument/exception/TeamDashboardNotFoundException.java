package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.NotFoundGroupException;

public class TeamDashboardNotFoundException extends NotFoundGroupException {

    public TeamDashboardNotFoundException(String message) {
        super(message);
    }

    public TeamDashboardNotFoundException() {
        this("존재하지 않는 팀 대시보드 입니다.");
    }
}
