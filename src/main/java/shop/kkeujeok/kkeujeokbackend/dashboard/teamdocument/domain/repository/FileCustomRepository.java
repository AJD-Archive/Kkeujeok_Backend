package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.File;

public interface FileCustomRepository {
    Page<File> findByFilesWithDocumentId(Long documentId, Pageable pageable);
}
