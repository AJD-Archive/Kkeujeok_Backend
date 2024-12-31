package shop.kkeujeok.kkeujeokbackend.amdin.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.kkeujeok.kkeujeokbackend.amdin.domain.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
