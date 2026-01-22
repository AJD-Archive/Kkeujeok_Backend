package shop.kkeujeok.kkeujeokbackend.challenge.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.global.entity.BaseEntity;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Entity
@Table(indexes = {
        @Index(name = "idx_challenge_member_mapping_member", columnList = "member_id"),
        @Index(name = "idx_challenge_member_mapping_challenge", columnList = "challenge_id"),
        @Index(name = "idx_challenge_member_mapping_composite", columnList = "member_id, challenge_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeMemberMapping extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "personal_dashboard_id")
    private PersonalDashboard personalDashboard;

    @JoinColumn(name = "is_completed")
    private boolean isCompleted;

    @Builder
    public ChallengeMemberMapping(Challenge challenge, Member member, PersonalDashboard personalDashboard,
                                  boolean isCompleted) {
        this.challenge = challenge;
        this.member = member;
        this.personalDashboard = personalDashboard;
        this.isCompleted = isCompleted;
    }

    public static ChallengeMemberMapping of(Challenge challenge, Member member, PersonalDashboard dashboard) {
        return ChallengeMemberMapping.builder()
                .challenge(challenge)
                .member(member)
                .personalDashboard(dashboard)
                .isCompleted(false)
                .build();
    }

    public void updateIsCompleted(boolean completed) {
        if (this.isCompleted == completed) {
            return;
        }
        this.isCompleted = completed;
    }
}
