package shop.kkeujeok.kkeujeokbackend.dashboard.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

public interface DashboardCustomRepository {

    Page<PersonalDashboard> findForPersonalDashboard(Member member, Pageable pageable);

    double calculateCompletionPercentage(Long dashboardId);
    
}
