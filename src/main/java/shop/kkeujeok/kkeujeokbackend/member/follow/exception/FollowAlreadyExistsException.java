package shop.kkeujeok.kkeujeokbackend.member.follow.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.AccessDeniedGroupException;

public class FollowAlreadyExistsException extends AccessDeniedGroupException {
    public FollowAlreadyExistsException(String message) {
        super(message);
    }

    public FollowAlreadyExistsException() {
        this("이미 친구를 요청했습니다.");
    }
}
