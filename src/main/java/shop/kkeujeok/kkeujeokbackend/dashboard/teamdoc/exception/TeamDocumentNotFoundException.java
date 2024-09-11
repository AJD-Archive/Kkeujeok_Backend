package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.NotFoundGroupException;

public class TeamDocumentNotFoundException extends NotFoundGroupException {

    public TeamDocumentNotFoundException(String message) {
        super(message);
    }

    public TeamDocumentNotFoundException() {
        this("존재하지 않는 팀문서 입니다.");
    }
}
