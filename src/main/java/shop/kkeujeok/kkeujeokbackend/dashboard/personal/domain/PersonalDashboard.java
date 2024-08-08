package shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.Dashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalDashboard extends Dashboard {

    public boolean isPublic;

    private String category;

    @Builder
    private PersonalDashboard(String title, String description, Member member, boolean isPublic, String category) {
        super(title, description, member);
        this.category = category;
        this.isPublic = isPublic;
    }

    public void update(String updateTitle, String updateDescription, String updateCategory) {
        super.update(updateTitle, updateDescription);
        this.category = updateCategory;
    }

}
