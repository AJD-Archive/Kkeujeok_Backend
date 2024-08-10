package shop.kkeujeok.kkeujeokbackend.block.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;

public interface BlockCustomRepository {
    Page<Block> findByBlockWithProgress(Long dashboardId, Progress progress, Pageable pageable);
}
