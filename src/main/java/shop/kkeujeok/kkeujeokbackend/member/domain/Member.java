package shop.kkeujeok.kkeujeokbackend.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    private boolean firstLogin;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;

    private String name;

    private String picture;

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    //    @Schema(description = "닉네임", example = "웅이")
    private String nickname;



    @Builder
    private Member(Role role, String email, String name, String picture, SocialType socialType, boolean firstLogin, String nickname) {
        this.role = role;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.socialType = socialType;
        this.firstLogin = firstLogin;
        this.nickname = nickname;
    }
}
