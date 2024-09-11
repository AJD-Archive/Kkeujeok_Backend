package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.domain.TeamDocument;

public interface TeamDocumentRepository extends JpaRepository<TeamDocument, Long>, TeamDocumentCustomRepository {

}
