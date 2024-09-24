package shop.kkeujeok.kkeujeokbackend.dashboard.team.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.InvalidGroupException;

public class AlreadyJoinedTeamException extends InvalidGroupException {
    public AlreadyJoinedTeamException(String message) {
        super(message);
    }

    public AlreadyJoinedTeamException() {
        this("이미 해당 팀에 참여한 멤버입니다.");
    }
}
