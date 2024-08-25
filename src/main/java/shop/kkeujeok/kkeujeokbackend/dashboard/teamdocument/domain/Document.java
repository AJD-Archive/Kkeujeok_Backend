package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamDashboard_id")
    private TeamDashboard teamDashboard;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

    @Builder
    private Document(String title, TeamDashboard teamDashboard) {
        this.status = Status.ACTIVE;
        this.title = title;
        this.teamDashboard = teamDashboard;
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
