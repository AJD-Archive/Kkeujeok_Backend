package shop.kkeujeok.kkeujeokbackend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shop.kkeujeok.kkeujeokbackend.global.annotationresolver.CurrentUserEmailArgumentResolver;

import java.util.List;

@Configuration
public class annotationWebConfig implements WebMvcConfigurer {

    private final CurrentUserEmailArgumentResolver currentUserEmailArgumentResolver;

    public annotationWebConfig(CurrentUserEmailArgumentResolver currentUserEmailArgumentResolver) {
        this.currentUserEmailArgumentResolver = currentUserEmailArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserEmailArgumentResolver);
    }
}

