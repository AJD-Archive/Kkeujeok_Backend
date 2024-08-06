package shop.kkeujeok.kkeujeokbackend.member.tag.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final Random random;

    public String getRandomTag() {
        return generateTag();
    }

    private String generateTag() {
        int randomNumber = 1000 + random.nextInt(9000);
        return "#" + randomNumber;
    }
}
