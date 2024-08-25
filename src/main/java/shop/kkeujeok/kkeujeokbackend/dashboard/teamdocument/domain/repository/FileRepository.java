package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.File;

public interface FileRepository extends JpaRepository<File, Long>, FileCustomRepository {
}
