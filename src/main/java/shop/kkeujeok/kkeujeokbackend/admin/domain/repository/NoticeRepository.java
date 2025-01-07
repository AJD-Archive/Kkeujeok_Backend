package shop.kkeujeok.kkeujeokbackend.admin.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.kkeujeok.kkeujeokbackend.admin.domain.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
