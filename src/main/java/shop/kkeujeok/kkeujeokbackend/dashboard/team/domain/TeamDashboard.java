package shop.kkeujeok.kkeujeokbackend.dashboard.team.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.Dashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.File;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamDashboard extends Dashboard {

    @OneToMany(mappedBy = "teamDashboard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamDashboardMemberMapping> teamDashboardMemberMappings = new ArrayList<>();

    @OneToMany(mappedBy = "teamDashboard", cascade = CascadeType.ALL)
    private List<Document> documents = new ArrayList<>();

    @Builder
    private TeamDashboard(String title, String description, String dType, Member member) {
        super(title, description, dType, member);
    }

    public void update(String updateTitle, String updateDescription) {
        super.update(updateTitle, updateDescription);
    }

    public void statusUpdate() {
        super.statusUpdate((super.getStatus() == Status.ACTIVE) ? Status.DELETED : Status.ACTIVE);
    }

    public void addMember(Member member) {
        teamDashboardMemberMappings.add(TeamDashboardMemberMapping.builder()
                .teamDashboard(this)
                .member(member)
                .build());
    }

    public void removeMember(Member member) {
        teamDashboardMemberMappings.removeIf(mapping -> mapping.getMember().equals(member));
    }

    public boolean hasMember(Member member) {
        return teamDashboardMemberMappings.stream()
                .anyMatch(mapping -> mapping.getMember().equals(member));
    }

}
