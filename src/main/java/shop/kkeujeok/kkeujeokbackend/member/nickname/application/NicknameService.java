package shop.kkeujeok.kkeujeokbackend.member.nickname.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional(readOnly = true)
public class NicknameService {
    private static final Set<String> usedNicknames = ConcurrentHashMap.newKeySet();
    private final List<String> adjectives;
    private final List<String> nouns;
    private final Random random;

    public NicknameService(List<String> adjectives, List<String> nouns, Random random) {
        this.adjectives = adjectives;
        this.nouns = nouns;
        this.random = random;
    }

    public String getRandomNickname() {
        int maxAttempts = adjectives.size() * nouns.size();
        return generateUniqueNickname(maxAttempts);
    }

    private String generateUniqueNickname(int maxAttempts) {
        String nickname;
        int attempts = 0;

        do {
            if (attempts >= maxAttempts) {
                throw new RuntimeException("닉네임 중복");
            }
            nickname = generateNickname();
            attempts++;
        } while (isNicknameUsed(nickname));

        markNicknameAsUsed(nickname);
        return nickname;
    }

    private String generateNickname() {
        String adjective = adjectives.get(random.nextInt(adjectives.size()));
        String noun = nouns.get(random.nextInt(nouns.size()));
        return adjective + " " + noun;
    }

    private boolean isNicknameUsed(String nickname) {
        return usedNicknames.contains(nickname);
    }

    private void markNicknameAsUsed(String nickname) {
        usedNicknames.add(nickname);
    }
}
