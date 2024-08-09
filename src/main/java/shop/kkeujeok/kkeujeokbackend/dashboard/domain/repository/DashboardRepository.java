package shop.kkeujeok.kkeujeokbackend.dashboard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.Dashboard;

public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
}
