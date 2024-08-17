package shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.repository.DashboardCustomRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;

public interface TeamDashboardRepository extends JpaRepository<TeamDashboard, Long>, DashboardCustomRepository {
}
