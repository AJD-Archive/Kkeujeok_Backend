package shop.kkeujeok.kkeujeokbackend.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.global.entity.BaseEntity;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.notification.domain.Notification;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Status status;

    private boolean firstLogin;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;

    private String name;

    private String picture;

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    private String nickname;

    private String introduction;

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Challenge> challenges = new ArrayList<>();

    private String tag;

    @JsonIgnore
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Notification> notifications = new ArrayList<>();

    @Builder
    private Member(Status status, Role role,
                   String email, String name,
                   String picture,
                   SocialType socialType,
                   boolean firstLogin,
                   String nickname,
                   String introduction,
                   String tag) {
        this.status = status;
        this.role = role;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.socialType = socialType;
        this.firstLogin = firstLogin;
        this.nickname = nickname;
        this.introduction = introduction;
        this.tag = tag;
    }

    public void update(String nickname, String introduction) {
        if (isUpdateRequired(nickname, introduction)) {
            this.nickname = nickname;
            this.introduction = introduction;
        }
    }

    private boolean isUpdateRequired(String updateNickname, String updateIntroduction) {
        return !this.nickname.equals(updateNickname) ||
                !this.introduction.equals(updateIntroduction);
    }
}
