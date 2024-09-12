package shop.kkeujeok.kkeujeokbackend.challenge.domain;

import lombok.Getter;

@Getter
public enum Cycle {
    DAILY("매일"),
    WEEKLY("매주"),
    MONTHLY("매달");

    private final String description;

    Cycle(String description) {
        this.description = description;
    }
}
