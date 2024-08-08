package shop.kkeujeok.kkeujeokbackend.challenge.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.AccessDeniedGroupException;

public class ChallengeAccessDeniedException extends AccessDeniedGroupException {
    public ChallengeAccessDeniedException(String message) {
        super(message);
    }

    public ChallengeAccessDeniedException() {
        this("챌린지 작성자가 아닙니다.");
    }
}
