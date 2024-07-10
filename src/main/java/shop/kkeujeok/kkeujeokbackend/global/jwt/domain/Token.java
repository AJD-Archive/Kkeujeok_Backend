package shop.kkeujeok.kkeujeokbackend.global.jwt.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tokenId")
    private Long tokenId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    private String refreshToken;

    @Builder
    private Token(Member member, String refreshToken) {
        this.member = member;
        this.refreshToken = refreshToken;
    }

    public void refreshTokenUpdate(String refreshToken) {
        if (!this.refreshToken.equals(refreshToken)) {
            this.refreshToken = refreshToken;
        }
    }
}
