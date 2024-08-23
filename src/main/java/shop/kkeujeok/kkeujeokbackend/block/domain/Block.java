package shop.kkeujeok.kkeujeokbackend.block.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.Dashboard;
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

    @Enumerated(value = EnumType.STRING)
    private Type type;

    private String startDate;

    private String deadLine;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_id")
    private Dashboard dashboard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Builder
    private Block(String title, String contents, Progress progress, Type type, Member member, String startDate,
                  String deadLine,
                  Dashboard dashboard, Challenge challenge) {
        this.status = Status.ACTIVE;
        this.title = title;
        this.contents = contents;
        this.progress = progress;
        this.type = type;
        this.startDate = startDate;
        this.deadLine = deadLine;
        this.member = member;
        this.dashboard = dashboard;
        this.challenge = challenge;
    }

    public void update(String updateTitle, String updateContents, String updateStartDate, String updateDeadLine) {
        if (isUpdateRequired(updateTitle, updateContents, updateStartDate, updateDeadLine)) {
            this.title = updateTitle;
            this.contents = updateContents;
            this.startDate = updateStartDate;
            this.deadLine = updateDeadLine;
        }
    }

    private boolean isUpdateRequired(String updateTitle, String updateContents, String updateStartDate,
                                     String updateDeadLine) {
        return !this.title.equals(updateTitle) ||
                !this.contents.equals(updateContents) ||
                !this.startDate.equals(updateStartDate) ||
                !this.deadLine.equals(updateDeadLine);
    }

    public void progressUpdate(Progress progress) {
        this.progress = progress;
    }

    public void statusUpdate() {
        this.status = (this.status == Status.ACTIVE) ? Status.DELETED : Status.ACTIVE;
    }

    public void updateChallengeStatus(Status status) {
        if (this.status == status) {
            return;
        }
        this.status = status;
    }
}
