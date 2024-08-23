package shop.kkeujeok.kkeujeokbackend.notification.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.global.entity.BaseEntity;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiver;

    private String message;

    @Column(nullable = false)
    private Boolean isRead;

    @Builder
    public Notification(Member receiver, String message, Boolean isRead) {
        this.receiver = receiver;
        this.message = message;
        this.isRead = isRead;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
