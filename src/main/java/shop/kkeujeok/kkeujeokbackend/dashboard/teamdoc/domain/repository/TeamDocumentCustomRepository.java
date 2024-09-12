package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.domain.TeamDocument;

import java.util.List;
import java.util.Set;

public interface TeamDocumentCustomRepository {

    Page<TeamDocument> findForTeamDocumentByCategory(TeamDashboard teamDashboard, String category, Pageable pageable);

    List<String> findTeamDocumentCategory(TeamDashboard teamDashboard);
}
