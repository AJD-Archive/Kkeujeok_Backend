package shop.kkeujeok.kkeujeokbackend.block.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;

public interface BlockRepository extends JpaRepository<Block, Long> {
}
