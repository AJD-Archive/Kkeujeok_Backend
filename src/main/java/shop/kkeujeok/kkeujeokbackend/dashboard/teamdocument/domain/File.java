package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.global.entity.BaseEntity;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private String email;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    @Builder
    private File(String email, String title, String content, Document document) {
        this.status = Status.ACTIVE;
        this.email = email;
        this.title = title;
        this.content = content;
        this.document = document;
    }

    public void update(String updateTitle, String updateContent) {
        if (isUpdateRequired(updateTitle, updateContent)) {
            this.title = updateTitle;
            this.content = updateContent;
        }
    }

    private boolean isUpdateRequired(String updateTitle, String updateContent) {
        return !this.title.equals(updateTitle) ||
                !this.content.equals(updateContent);
    }

    public void statusUpdate() {
        this.status = (this.status == Status.ACTIVE) ? Status.DELETED : Status.ACTIVE;
    }
}
