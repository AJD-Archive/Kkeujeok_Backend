package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.NotFoundGroupException;

public class FileNotFoundException extends NotFoundGroupException {

    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException() {
        this("존재하지 않는 팀 파일 입니다.");
    }
}
