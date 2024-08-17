package shop.kkeujeok.kkeujeokbackend.challenge.exception;

import shop.kkeujeok.kkeujeokbackend.global.error.exception.InvalidGroupException;

public class InvalidCycleDetailsConversionException extends InvalidGroupException {
    private static final String CONVERSION_TO_DATABASE_COLUMN_MESSAGE =
            "List<CycleDetail>를 JSON 문자열로 변환하는 중 오류가 발생했습니다.";
    private static final String CONVERSION_TO_ENTITY_ATTRIBUTE_MESSAGE =
            "JSON 문자열을 List<CycleDetail>로 변환하는 중 오류가 발생했습니다.";

    public InvalidCycleDetailsConversionException(String message) {
        super(message);
    }

    public static InvalidCycleDetailsConversionException forConversionToDatabaseColumn() {
        return new InvalidCycleDetailsConversionException(CONVERSION_TO_DATABASE_COLUMN_MESSAGE);
    }

    public static InvalidCycleDetailsConversionException forConversionToEntityAttribute() {
        return new InvalidCycleDetailsConversionException(CONVERSION_TO_ENTITY_ATTRIBUTE_MESSAGE);
    }
}
