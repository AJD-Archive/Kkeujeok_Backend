package shop.kkeujeok.kkeujeokbackend.challenge.domain;

import jakarta.persistence.Entity;
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
public class ChallengeMemberMapping extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @JoinColumn(name = "is_completed")
    private boolean isCompleted;

    @Builder
    public ChallengeMemberMapping(Challenge challenge, Member member, boolean isCompleted) {
        this.challenge = challenge;
        this.member = member;
        this.isCompleted = isCompleted;
    }

    public static ChallengeMemberMapping of(Challenge challenge, Member member) {
        return ChallengeMemberMapping.builder()
                .challenge(challenge)
                .member(member)
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
