package shop.kkeujeok.kkeujeokbackend.member.nickname.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.nickname.exception.TimeOutException;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NicknameService {

    private final List<String> adjectives;
    private final List<String> nouns;
    private final Random random;

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
