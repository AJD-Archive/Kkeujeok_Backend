package shop.kkeujeok.kkeujeokbackend.dashboard.domain.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

public interface DashboardCustomRepository {

    Page<PersonalDashboard> findForPersonalDashboard(Member member, Pageable pageable);

    List<String> findForPersonalDashboardByCategory(Member member);

    Page<TeamDashboard> findForTeamDashboard(Member member, Pageable pageable);

    List<Member> findForMembersByQuery(String query);

    double calculateCompletionPercentage(Long dashboardId);

}
