package shop.kkeujeok.kkeujeokbackend.dashboard.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.InvalidGroupException;

public class InvalidMemberInviteException extends InvalidGroupException {
    public InvalidMemberInviteException(String message) {
        super(message);
    }

    public InvalidMemberInviteException() {
        this("같은 계정을 초대할 수 없습니다");
    }
}
