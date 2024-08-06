package shop.kkeujeok.kkeujeokbackend.member.tag.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final Random random;
    private final MemberRepository memberRepository;

    public String getRandomTag(String nickname) {
        return generateUniqueTag(nickname);
    }

    private String generateUniqueTag(String nickname) {
        String tag = generateTag();
        if (memberRepository.existsByNicknameAndTag(nickname, tag)) {
            return generateUniqueTag(nickname);
        }
        return tag;
    }

    private String generateTag() {
        int randomNumber = 1000 + random.nextInt(9000);
        return "#" + randomNumber;
    }
}
