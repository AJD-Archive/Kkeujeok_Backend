package shop.kkeujeok.kkeujeokbackend.block.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.NotFoundGroupException;

public class BlockNotFoundException extends NotFoundGroupException {

    public BlockNotFoundException(String message) {
        super(message);
    }

    public BlockNotFoundException() {
        this("존재하지 않는 블록 입니다.");
    }
}
