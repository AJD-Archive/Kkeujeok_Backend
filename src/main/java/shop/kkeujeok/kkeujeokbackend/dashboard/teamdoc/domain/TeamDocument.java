package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.global.entity.BaseEntity;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamDocument extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private String author;

    private String picture;

    private String title;

    private String content;

    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TeamDashboard_id")
    private TeamDashboard TeamDashboard;

    @Builder
    private TeamDocument(String author,
                         String picture,
                         String title,
                         String content,
                         String category,
                         TeamDashboard teamDashboard) {
        this.status = Status.ACTIVE;
        this.author = author;
        this.picture = picture;
        this.title = title;
        this.content = content;
        this.category = category;
        this.TeamDashboard = teamDashboard;
    }

    public void update(String updateTitle, String updateContent, String updateCategory) {
        this.title = updateTitle;
        this.content = updateContent;
        this.category = updateCategory;
    }

    public void statusUpdate() {
        this.status = (this.status == Status.ACTIVE) ? Status.DELETED : Status.ACTIVE;
    }
}
