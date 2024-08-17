package shop.kkeujeok.kkeujeokbackend.challenge.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.NotFoundGroupException;

public class ChallengeNotFoundException extends NotFoundGroupException {
    public ChallengeNotFoundException(String message) {
        super(message);
    }

    public ChallengeNotFoundException() {
        this("존재하지 않는 챌린지입니다.");
    }
}
