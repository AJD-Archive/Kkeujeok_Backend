package shop.kkeujeok.kkeujeokbackend.block.domain;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.global.entity.BaseEntity;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Block extends BaseEntity {

    private Status status;

    private String title;

    private String contents;

    private Progress progress;

    @Builder
    private Block(String title, String contents, Progress progress) {
        this.status = Status.A;
        this.title = title;
        this.contents = contents;
        this.progress = progress;
    }

}
