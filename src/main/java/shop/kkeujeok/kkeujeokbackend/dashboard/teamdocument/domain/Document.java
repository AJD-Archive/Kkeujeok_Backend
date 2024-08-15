package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.global.entity.BaseEntity;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Document extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private String title;

    // 팀 대시보드 연관관계

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

    @Builder
    private Document(String title) {
        this.status = Status.ACTIVE;
        this.title = title;
        // 팀 대시보드 설정
    }

    public void update(String updateTitle) {
        if (isUpdateRequired(updateTitle)) {
            this.title = updateTitle;
        }
    }

    private boolean isUpdateRequired(String updateTitle) {
        return !this.title.equals(updateTitle);
    }

    public void statusUpdate() {
        this.status = (this.status == Status.ACTIVE) ? Status.DELETED : Status.ACTIVE;
    }
}
