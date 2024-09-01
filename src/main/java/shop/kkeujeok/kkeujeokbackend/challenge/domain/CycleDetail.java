package shop.kkeujeok.kkeujeokbackend.challenge.domain;

import lombok.Getter;

@Getter
public enum CycleDetail {
    DAILY("매일", Cycle.DAILY, 0),

    MON("월요일", Cycle.WEEKLY, 1), TUE("화요일", Cycle.WEEKLY, 2), WED("수요일", Cycle.WEEKLY, 3),
    THU("목요일", Cycle.WEEKLY, 4), FRI("금요일", Cycle.WEEKLY, 5), SAT("토요일", Cycle.WEEKLY, 6),
    SUN("일요일", Cycle.WEEKLY, 7),

    FIRST("1일", Cycle.MONTHLY, 1), SECOND("2일", Cycle.MONTHLY, 2), THIRD("3일", Cycle.MONTHLY, 3),
    FOURTH("4일", Cycle.MONTHLY, 4), FIFTH("5일", Cycle.MONTHLY, 5), SIXTH("6일", Cycle.MONTHLY, 6),
    SEVENTH("7일", Cycle.MONTHLY, 7), EIGHTH("8일", Cycle.MONTHLY, 8), NINTH("9일", Cycle.MONTHLY, 9),
    TENTH("10일", Cycle.MONTHLY, 10), ELEVENTH("11일", Cycle.MONTHLY, 11), TWELFTH("12일", Cycle.MONTHLY, 12),
    THIRTEENTH("13일", Cycle.MONTHLY, 13), FOURTEENTH("14일", Cycle.MONTHLY, 14), FIFTEENTH("15일", Cycle.MONTHLY,
            15),
    SIXTEENTH("16일", Cycle.MONTHLY, 16), SEVENTEENTH("17일", Cycle.MONTHLY, 17), EIGHTEENTH("18일", Cycle.MONTHLY,
            18),
    NINETEENTH("19일", Cycle.MONTHLY, 19), TWENTIETH("20일", Cycle.MONTHLY, 20), TWENTY_FIRST("21일", Cycle.MONTHLY,
            21),
    TWENTY_SECOND("22일", Cycle.MONTHLY, 22), TWENTY_THIRD("23일", Cycle.MONTHLY,
            23), TWENTY_FOURTH("24일", Cycle.MONTHLY, 24),
    TWENTY_FIFTH("25일", Cycle.MONTHLY, 25), TWENTY_SIXTH("26일", Cycle.MONTHLY,
            26), TWENTY_SEVENTH("27일", Cycle.MONTHLY, 27),
    TWENTY_EIGHTH("28일", Cycle.MONTHLY, 28), TWENTY_NINTH("29일", Cycle.MONTHLY,
            29), THIRTIETH("30일", Cycle.MONTHLY, 30),
    THIRTY_FIRST("31일", Cycle.MONTHLY, 31);

    private final String description;
    private final Cycle cycle;
    private final int value;

    CycleDetail(String description, Cycle cycle, int value) {
        this.description = description;
        this.cycle = cycle;
        this.value = value;
    }
}
