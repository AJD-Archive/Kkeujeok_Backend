package shop.kkeujeok.kkeujeokbackend.block.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Type;

public interface BlockRepository extends JpaRepository<Block, Long>, BlockCustomRepository {
    List<Block> findByType(Type type);
}
