package shop.kkeujeok.kkeujeokbackend.block.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.global.entity.BaseEntity;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Block extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @Enumerated(value = EnumType.STRING)
    private Progress progress;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Block(String title, String contents, Progress progress, Member member) {
        this.status = Status.A;
        this.title = title;
        this.contents = contents;
        this.progress = progress;
        this.member = member;
    }

}
