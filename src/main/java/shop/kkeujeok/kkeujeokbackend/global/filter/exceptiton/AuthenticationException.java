package shop.kkeujeok.kkeujeokbackend.global.filter.exceptiton;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.AuthGroupException;

public class AuthenticationException extends AuthGroupException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException() {
        this("인증에 실패했습니다. 자격 증명을 확인하고 다시 시도하십시오.");
    }
}
