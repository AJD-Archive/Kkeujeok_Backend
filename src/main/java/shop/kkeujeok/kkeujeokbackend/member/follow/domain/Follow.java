package shop.kkeujeok.kkeujeokbackend.member.follow.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.global.entity.BaseEntity;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Follow extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "from_member")
    private Member fromMember;

    @ManyToOne
    @JoinColumn(name = "to_member")
    private Member toMember;

    @Enumerated(EnumType.STRING)
    private FollowStatus followStatus;
}
