package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.NotFoundGroupException;

public class DocumentNotFoundException extends NotFoundGroupException {

    public DocumentNotFoundException(String message) {
        super(message);
    }

    public DocumentNotFoundException() {
        this("존재하지 않는 팀 문서 입니다.");
    }
}
