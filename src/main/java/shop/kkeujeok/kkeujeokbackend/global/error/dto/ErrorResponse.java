package shop.kkeujeok.kkeujeokbackend.global.error.dto;

public record ErrorResponse(
        int statusCode,
        String message
) {
}