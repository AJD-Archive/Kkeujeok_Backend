package shop.kkeujeok.kkeujeokbackend.member.nickname.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NicknameServiceTest {

    @Mock
    private Random random;

    @InjectMocks
    private NicknameService nicknameService;

    @BeforeEach
    void setUp() {
        nicknameService = new NicknameService(
                List.of("행복한", "귀여운", "깜찍한"),
                List.of("고양이", "인호", "토끼"),
                random
        );
    }

    @DisplayName("랜덤 닉네임을 생성합니다.")
    @Test
    void 랜덤_닉네임을_생성합니다() {
        when(random.nextInt(anyInt())).thenReturn(0, 1);

        String nickname = nicknameService.getRandomNickname();

        assertThat(nickname).isEqualTo("행복한인호");
        verify(random, times(2)).nextInt(anyInt());
    }
}
