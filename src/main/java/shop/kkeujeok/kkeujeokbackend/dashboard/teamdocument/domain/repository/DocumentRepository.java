package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;

public interface DocumentRepository extends JpaRepository<Document, Long>, DocumentCustomRepository {
}
