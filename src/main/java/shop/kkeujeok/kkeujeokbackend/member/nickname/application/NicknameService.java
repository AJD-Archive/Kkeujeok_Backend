package shop.kkeujeok.kkeujeokbackend.member.nickname.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NicknameService {
    private final List<String> adjectives = List.of(
            "귀여운 ", "행복한 ", "깜찍한 ", "명랑한 ", "재미있는 ",
            "용감한 ", "사려깊은 ", "활기찬 ", "사랑스러운 ", "친절한 ",
            "밝은 ", "기분좋은 ", "즐거운 ", "신나는 ", "멋진 "
    );

    private final List<String> nouns = List.of(
            "고양이", "강아지", "토끼", "곰", "여우",
            "판다", "호랑이", "사자", "다람쥐", "고슴도치",
            "햄스터", "펭귄", "수달", "부엉이", "돌고래"
    );
    private final Random random = new Random();
    private final Set<String> usedNicknames = new HashSet<>();

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
