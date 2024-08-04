package shop.kkeujeok.kkeujeokbackend.member.nickname.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
public class NicknameService {

    private final List<String> adjectives;
    private final List<String> nouns;
    private final Random random;

    public NicknameService(@Qualifier("adjectives") List<String> adjectives,
                           @Qualifier("nouns") List<String> nouns,
                           Random random) {
        this.adjectives = adjectives;
        this.nouns = nouns;
        this.random = random;
    }

    public String getRandomNickname() {
        return generateNickname();
    }

    private String generateNickname() {
        String adjective = getRandomElement(adjectives);
        String noun = getRandomElement(nouns);
        return adjective + noun;
    }

    private String getRandomElement(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }
}
