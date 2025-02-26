package shop.kkeujeok.kkeujeokbackend.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {

    private String version;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @Builder
    private Notice(String version, String title, String contents) {
        this.version = version;
        this.title = title;
        this.contents = contents;
    }

}
