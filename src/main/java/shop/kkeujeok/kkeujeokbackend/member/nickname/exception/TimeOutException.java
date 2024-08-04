package shop.kkeujeok.kkeujeokbackend.member.nickname.exception;

public class TimeOutException extends RuntimeException {
    public TimeOutException(String message) {
        super(message);
    }

    public TimeOutException() {
        this("고유한 닉네임을 생성할 수 없습니다.(시간 초과)");
    }
}
