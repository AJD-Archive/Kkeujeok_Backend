package shop.kkeujeok.kkeujeokbackend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public List<String> adjectives() {
        return List.of(
                "귀여운", "행복한", "깜찍한", "명랑한", "재미있는",
                "용감한", "사려깊은", "활기찬", "사랑스러운", "친절한",
                "밝은", "기분좋은", "즐거운", "신나는", "멋진"
        );
    }

    @Bean
    public List<String> nouns() {
        return List.of(
                "고양이", "강아지", "토끼", "곰", "여우",
                "판다", "호랑이", "사자", "다람쥐", "고슴도치",
                "햄스터", "펭귄", "수달", "부엉이", "돌고래"
        );
    }
}

