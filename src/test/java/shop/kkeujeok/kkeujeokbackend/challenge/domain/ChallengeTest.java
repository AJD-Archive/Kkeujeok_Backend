package shop.kkeujeok.kkeujeokbackend.challenge.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.kkeujeok.kkeujeokbackend.challenge.exception.InvalidCycleException;

class ChallengeTest {

    private Challenge challenge;

    @BeforeEach
    void setUp() {
        challenge = Challenge.builder()
                .title("테스트 챌린지")
                .contents("테스트")
                .cycleDetails(Arrays.asList(CycleDetail.MON, CycleDetail.TUE, CycleDetail.WED))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .representImage("대표이미지")
                .member(null)
                .build();
    }

    @Test
    @DisplayName("중복된 CycleDetail이 있으면 예외가 발생한다")
    void 중복된_CycleDetail이_있으면_예외가_발생한다() {
        // given
        List<CycleDetail> cycleDetails = Arrays.asList(
                CycleDetail.MON,
                CycleDetail.MON
        );
        challenge = Challenge.builder()
                .title("테스트 챌린지")
                .contents("테스트")
                .cycleDetails(cycleDetails)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .representImage("대표이미지")
                .member(null)
                .build();

        // when & then
        assertThatThrownBy(() -> challenge.validateCycleDetails(Cycle.WEEKLY))
                .isInstanceOf(InvalidCycleException.class)
                .hasMessageContaining("중복된 주기 세부 항목이 발견되었습니다");
    }

    @Test
    @DisplayName("주기와 일치하지 않는 CycleDetail이 있으면 예외가 발생한다")
    void 주기와_일치하지_않는_CycleDetail이_있으면_예외가_발생한다() {
        // given
        List<CycleDetail> cycleDetails = Arrays.asList(
                CycleDetail.MON,
                CycleDetail.FIRST // 주기와 일치하지 않음
        );
        challenge = Challenge.builder()
                .title("테스트 챌린지")
                .contents("테스트")
                .cycleDetails(cycleDetails)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .representImage("대표이미지")
                .member(null) // Member는 null로 설정
                .build();

        // when & then
        assertThatThrownBy(() -> challenge.validateCycleDetails(Cycle.WEEKLY))
                .isInstanceOf(InvalidCycleException.class)
                .hasMessageContaining("주기와 일치하지 않는 세부 항목이 있습니다");
    }

    @Test
    @DisplayName("모든 CycleDetail이 유효한 경우 예외가 발생하지 않는다")
    void 모든_CycleDetail이_유효한_경우_예외가_발생하지_않는다() {
        // given
        List<CycleDetail> cycleDetails = Arrays.asList(
                CycleDetail.MON,
                CycleDetail.TUE,
                CycleDetail.WED
        );
        challenge = Challenge.builder()
                .title("테스트 챌린지")
                .contents("테스트")
                .cycleDetails(cycleDetails)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .representImage("대표이미지")
                .member(null)
                .build();

        // when & then
        challenge.validateCycleDetails(Cycle.WEEKLY);
    }
}
