package shop.kkeujeok.kkeujeokbackend.member.follow.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.NotFoundGroupException;

public class FollowNotFoundException extends NotFoundGroupException {
    public FollowNotFoundException(String message) {
        super(message);
    }

    public FollowNotFoundException() {
        this("존재하지 않는 팔로워(회원)입니다");
    }
}
