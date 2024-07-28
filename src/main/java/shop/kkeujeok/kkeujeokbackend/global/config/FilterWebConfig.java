package shop.kkeujeok.kkeujeokbackend.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.kkeujeok.kkeujeokbackend.global.filter.LogFilter;
import shop.kkeujeok.kkeujeokbackend.global.filter.LoginCheckFilter;
import shop.kkeujeok.kkeujeokbackend.global.jwt.TokenProvider;

@Configuration
public class FilterWebConfig {
    private final TokenProvider tokenProvider;

    public FilterWebConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public FilterRegistrationBean<LogFilter> logFilter() {
        FilterRegistrationBean<LogFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); // 여기서 만든 필터 클래스 등록
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<LoginCheckFilter> loginCheckFilter() {
        FilterRegistrationBean<LoginCheckFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter(tokenProvider)); // JWT 토큰 유효성 검사를 위한 필터 클래스 등록
        filterRegistrationBean.setOrder(2); // 1번인 로그필터 다음으로 수행
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}