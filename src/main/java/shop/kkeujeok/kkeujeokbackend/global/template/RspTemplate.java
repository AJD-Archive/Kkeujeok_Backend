package shop.kkeujeok.kkeujeokbackend.global.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 응답 템플릿
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RspTemplate<T> {
    int statusCode;
    String message;
    T data;

    public RspTemplate(HttpStatus httpStatus, String message, T data) {
        this.statusCode = httpStatus.value();
        this.message = message;
        this.data = data;
    }

    public RspTemplate(HttpStatus httpStatus, String message) {
        this.statusCode = httpStatus.value();
        this.message = message;
    }
}
