package shop.kkeujeok.kkeujeokbackend.challenge.application.util;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Cycle;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;

public class ChallengeBlockStatusUtil {
    @Getter
    private static final ChallengeBlockStatusUtil instance = new ChallengeBlockStatusUtil();

    private ChallengeBlockStatusUtil() {
    }

    public Boolean isChallengeBlockActiveToday(Cycle cycle, List<CycleDetail> cycleDetails) {
        LocalDate today = LocalDate.now();
        int dayOfWeekNumber = today.getDayOfWeek().getValue();

        return switch (cycle) {
            case DAILY -> true;
            case WEEKLY -> {
                List<Integer> activeDays = cycleDetails.stream()
                        .map(CycleDetail::getValue)
                        .toList();
                yield activeDays.contains(dayOfWeekNumber);
            }
            case MONTHLY -> {
                List<Integer> activeDays = cycleDetails.stream()
                        .map(CycleDetail::getValue)
                        .toList();
                yield activeDays.contains(today.getDayOfMonth());
            }
        };
    }
}
