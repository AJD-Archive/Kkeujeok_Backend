package shop.kkeujeok.kkeujeokbackend.global.error.exception;

public abstract class InvalidGroupException extends RuntimeException{
    public InvalidGroupException(String message) {
        super(message);
    }
}
