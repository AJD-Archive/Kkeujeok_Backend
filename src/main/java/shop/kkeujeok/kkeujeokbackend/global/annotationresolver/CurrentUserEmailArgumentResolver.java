package shop.kkeujeok.kkeujeokbackend.global.annotationresolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.TokenReqDto;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.jwt.TokenProvider;

@Component
public class CurrentUserEmailArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;

    public CurrentUserEmailArgumentResolver(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUserEmail.class) != null;
        //@CurrentUserEmail 어노테이션으로 주석되어 있는지 확인하는 로직.
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 이후의 토큰 부분만 추출하고
            TokenReqDto tokenReqDto = new TokenReqDto(token);

            return tokenProvider.getUserEmailFromToken(tokenReqDto); // 만들어둔 메서드로 email 값 반환
        }

        return null;
    }
}
