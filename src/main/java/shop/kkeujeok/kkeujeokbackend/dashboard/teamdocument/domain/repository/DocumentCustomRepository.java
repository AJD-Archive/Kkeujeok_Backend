package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;

public interface DocumentCustomRepository {
    Page<Document> findByDocumentWithTeamDashboard(Long documentId, Pageable pageable);
}
