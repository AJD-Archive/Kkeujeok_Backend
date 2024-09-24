package shop.kkeujeok.kkeujeokbackend.dashboard.domain.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboardMemberMapping;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

public interface DashboardCustomRepository {

    List<PersonalDashboard> findForPersonalDashboard(Member member);

    Page<PersonalDashboard> findForPersonalDashboard(Member member, Pageable pageable);

    Set<String> findCategoriesForDashboard(Member member);

    Page<TeamDashboard> findForTeamDashboard(Member member, Pageable pageable);

    List<TeamDashboard> findForTeamDashboard(Member member);

    List<Member> findForMembersByQuery(String query);

    double calculateCompletionPercentage(Long dashboardId);

    Page<PersonalDashboard> findPublicPersonalDashboard(Member member, Pageable pageable);
}
