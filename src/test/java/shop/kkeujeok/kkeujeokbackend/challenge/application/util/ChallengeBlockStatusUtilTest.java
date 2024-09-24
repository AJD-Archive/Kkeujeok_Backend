package shop.kkeujeok.kkeujeokbackend.challenge.application.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Cycle;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;

class ChallengeBlockStatusUtilTest {

    ChallengeBlockStatusUtil challengeBlockStatusUtil = ChallengeBlockStatusUtil.getInstance();

    @Test
    @DisplayName("DAILY 주기는 항상 true를 반환한다")
    void DAILY_주기는_항상_true를_반환한다() {
        // given
        List<CycleDetail> cycleDetails = List.of(CycleDetail.DAILY);

        // when
        Boolean result = challengeBlockStatusUtil.isChallengeBlockActiveToday(Cycle.DAILY, cycleDetails);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("WEEKLY 주기에서 오늘의 요일이 포함되어 있으면 true를 반환한다")
    void WEEKLY_주기에서_오늘의_요일이_포함되어_있으면_true를_반환한다() {
        // given
        List<CycleDetail> activeDays = List.of(CycleDetail.MON, CycleDetail.TUE, CycleDetail.WED, CycleDetail.THU,
                CycleDetail.FRI, CycleDetail.SAT, CycleDetail.SUN);

        // when
        Boolean result = challengeBlockStatusUtil.isChallengeBlockActiveToday(Cycle.WEEKLY, activeDays);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("WEEKLY 주기에서 오늘의 요일이 포함되지 않으면 false를 반환한다")
    void WEEKLY_주기에서_오늘의_요일이_포함되지_않으면_비활성_상태여야_한다() {
        // given
        List<CycleDetail> activeDays = List.of();

        // when
        Boolean result = challengeBlockStatusUtil.isChallengeBlockActiveToday(Cycle.WEEKLY, activeDays);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("MONTHLY 주기에서 오늘의 날짜가 포함되어 있으면 true를 반환한다.")
    void MONTHLY_주기에서_오늘의_날짜가_포함되어_있으면_true를_반환한다() {
        // given
        List<CycleDetail> activeDays = List.of(
                CycleDetail.FIRST, CycleDetail.SECOND, CycleDetail.THIRD, CycleDetail.FOURTH, CycleDetail.FIFTH,
                CycleDetail.SIXTH, CycleDetail.SEVENTH, CycleDetail.EIGHTH, CycleDetail.NINTH, CycleDetail.TENTH,
                CycleDetail.ELEVENTH, CycleDetail.TWELFTH, CycleDetail.THIRTEENTH, CycleDetail.FOURTEENTH,
                CycleDetail.FIFTEENTH,
                CycleDetail.SIXTEENTH, CycleDetail.SEVENTEENTH, CycleDetail.EIGHTEENTH, CycleDetail.NINETEENTH,
                CycleDetail.TWENTIETH,
                CycleDetail.TWENTY_FIRST, CycleDetail.TWENTY_SECOND, CycleDetail.TWENTY_THIRD,
                CycleDetail.TWENTY_FOURTH, CycleDetail.TWENTY_FIFTH,
                CycleDetail.TWENTY_SIXTH, CycleDetail.TWENTY_SEVENTH, CycleDetail.TWENTY_EIGHTH,
                CycleDetail.TWENTY_NINTH, CycleDetail.THIRTIETH,
                CycleDetail.THIRTY_FIRST);

        // when
        Boolean result = challengeBlockStatusUtil.isChallengeBlockActiveToday(Cycle.MONTHLY, activeDays);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("MONTHLY 주기에서 오늘의 날짜가 포함되지 않으면 false를 반환한다")
    void MONTHLY_주기에서_오늘의_날짜가_포함되지_않으면_false를_반환한다() {
        // given
        List<CycleDetail> activeDays = List.of();

        // when
        Boolean result = challengeBlockStatusUtil.isChallengeBlockActiveToday(Cycle.MONTHLY, activeDays);

        // then
        assertThat(result).isFalse();
    }
}
