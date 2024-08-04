package shop.kkeujeok.kkeujeokbackend.challenge.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.InvalidGroupException;

public class InvalidCycleDetailsConversionException extends InvalidGroupException{
    public InvalidCycleDetailsConversionException(String message) {
        super(message);
    }
}