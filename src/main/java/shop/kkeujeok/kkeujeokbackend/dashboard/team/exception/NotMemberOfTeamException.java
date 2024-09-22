package shop.kkeujeok.kkeujeokbackend.dashboard.team.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.InvalidGroupException;

public class NotMemberOfTeamException extends InvalidGroupException {
    public NotMemberOfTeamException(String message) {
        super(message);
    }

    public NotMemberOfTeamException() {
        this("해당 멤버는 팀에 속해 있지 않습니다.");
    }
}
