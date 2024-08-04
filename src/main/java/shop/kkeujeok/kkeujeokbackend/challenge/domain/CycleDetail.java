package shop.kkeujeok.kkeujeokbackend.challenge.domain;

import lombok.Getter;

@Getter
public enum CycleDetail {
    DAILY("매일", Cycle.DAILY),

    MON("월요일", Cycle.WEEKLY), TUE("화요일", Cycle.WEEKLY), WED("수요일", Cycle.WEEKLY),
    THU("목요일", Cycle.WEEKLY), FRI("금요일", Cycle.WEEKLY), SAT("토요일", Cycle.WEEKLY),
    SUN("일요일", Cycle.WEEKLY),

    FIRST("1일", Cycle.MONTHLY), SECOND("2일", Cycle.MONTHLY), THIRD("3일", Cycle.MONTHLY),
    FOURTH("4일", Cycle.MONTHLY), FIFTH("5일", Cycle.MONTHLY), SIXTH("6일", Cycle.MONTHLY),
    SEVENTH("7일", Cycle.MONTHLY), EIGHTH("8일", Cycle.MONTHLY), NINTH("9일", Cycle.MONTHLY),
    TENTH("10일", Cycle.MONTHLY), ELEVENTH("11일", Cycle.MONTHLY), TWELFTH("12일", Cycle.MONTHLY),
    THIRTEENTH("13일", Cycle.MONTHLY), FOURTEENTH("14일", Cycle.MONTHLY), FIFTEENTH("15일", Cycle.MONTHLY),
    SIXTEENTH("16일", Cycle.MONTHLY), SEVENTEENTH("17일", Cycle.MONTHLY), EIGHTEENTH("18일", Cycle.MONTHLY),
    NINETEENTH("19일", Cycle.MONTHLY), TWENTIETH("20일", Cycle.MONTHLY), TWENTY_FIRST("21일", Cycle.MONTHLY),
    TWENTY_SECOND("22일", Cycle.MONTHLY), TWENTY_THIRD("23일", Cycle.MONTHLY), TWENTY_FOURTH("24일", Cycle.MONTHLY),
    TWENTY_FIFTH("25일", Cycle.MONTHLY), TWENTY_SIXTH("26일", Cycle.MONTHLY), TWENTY_SEVENTH("27일", Cycle.MONTHLY),
    TWENTY_EIGHTH("28일", Cycle.MONTHLY), TWENTY_NINTH("29일", Cycle.MONTHLY), THIRTIETH("30일", Cycle.MONTHLY),
    THIRTY_FIRST("31일", Cycle.MONTHLY);

    private final String description;
    private final Cycle cycle;

    CycleDetail(String description, Cycle cycle) {
        this.description = description;
        this.cycle = cycle;
    }
}
