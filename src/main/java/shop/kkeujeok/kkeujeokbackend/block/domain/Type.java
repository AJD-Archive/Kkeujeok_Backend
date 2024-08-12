package shop.kkeujeok.kkeujeokbackend.block.domain;

import lombok.Getter;

@Getter
public enum Type {
    BASIC("일반 블록"),
    CHALLENGE("챌린지 블록");

    private final String description;

    Type(String description) {
        this.description = description;
    }
}
