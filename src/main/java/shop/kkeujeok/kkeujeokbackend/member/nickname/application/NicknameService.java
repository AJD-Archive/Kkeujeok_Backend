package shop.kkeujeok.kkeujeokbackend.member.nickname.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;

import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
public class NicknameService {
    private final List<String> adjectives;
    private final List<String> nouns;
    private final Random random;
    private final MemberRepository memberRepository;

    public NicknameService(List<String> adjectives, List<String> nouns, Random random, MemberRepository memberRepository) {
        this.adjectives = adjectives;
        this.nouns = nouns;
        this.random = random;
        this.memberRepository = memberRepository;
    }

    public String getRandomNickname() {
        int maxAttempts = getMaxAttempts();
        return generateUniqueNickname(maxAttempts);
    }

    private int getMaxAttempts() {
        return adjectives.size() * nouns.size();
    }

    private String generateUniqueNickname(int maxAttempts) {
        int attempts = 0;
        String nickname;

        do {
            if (attempts >= maxAttempts) {
                throw new RuntimeException("고유한 닉네임을 생성할 수 없습니다.");
            }
            nickname = generateNickname();
            attempts++;
        } while (isNicknameUsed(nickname));

        return nickname;
    }

    private String generateNickname() {
        String adjective = getRandomElement(adjectives);
        String noun = getRandomElement(nouns);
        return adjective + " " + noun;
    }

    private String getRandomElement(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }

    private boolean isNicknameUsed(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
