package shop.kkeujeok.kkeujeokbackend.block.domain;

import lombok.Getter;

@Getter
public enum Progress {
    NOT_STARTED("시작 전"),
    IN_PROGRESS("진행 중"),
    COMPLETED("완료");

    private final String description;

    Progress(String description) {
        this.description = description;
    }

}
