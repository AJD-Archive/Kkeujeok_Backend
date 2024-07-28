package shop.kkeujeok.kkeujeokbackend.block.domain.repository;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.global.config.JpaAuditingConfig;
import shop.kkeujeok.kkeujeokbackend.global.config.QuerydslConfig;

@DataJpaTest
@Import({JpaAuditingConfig.class, QuerydslConfig.class})
@ActiveProfiles("test")
class BlockRepositoryTest {

    @Autowired
    private BlockRepository blockRepository;

    @DisplayName("블록을 진행 상태별로 전체 조회합니다.")
    @Test
    void 블록_진행_상태_전체_조회() {
        // given
        Block block1 = Block.builder()
                .title("title1")
                .contents("contents1")
                .progress(Progress.NOT_STARTED)
                .deadLine("2024.07.27 11:03")
                .build();

        Block block2 = Block.builder()
                .title("title2")
                .contents("contents2")
                .progress(Progress.NOT_STARTED)
                .deadLine("2024.07.27 11:04")
                .build();

        Block block3 = Block.builder()
                .title("title3")
                .contents("contents3")
                .progress(Progress.IN_PROGRESS)
                .deadLine("2024.07.27 11:05")
                .build();

        blockRepository.save(block1);
        blockRepository.save(block2);
        blockRepository.save(block3);

        Progress progress = Progress.NOT_STARTED;
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Block> blocks = blockRepository.findByBlockWithProgress(progress, pageable);

        // then
        assertThat(blocks.getContent().size()).isEqualTo(2);
    }

    @DisplayName("블록을 논리 삭제 상태별로 전체 조회합니다.")
    @Test
    void 블록_삭제_상태_전체_조회() {
        // given
        Block block1 = Block.builder()
                .title("title1")
                .contents("contents1")
                .progress(Progress.NOT_STARTED)
                .deadLine("2024.07.27 11:03")
                .build();

        Block block2 = Block.builder()
                .title("title2")
                .contents("contents2")
                .progress(Progress.NOT_STARTED)
                .deadLine("2024.07.27 11:04")
                .build();

        Block block3 = Block.builder()
                .title("title3")
                .contents("contents3")
                .progress(Progress.IN_PROGRESS)
                .deadLine("2024.07.27 11:05")
                .build();

        blockRepository.save(block1);
        blockRepository.save(block2);
        blockRepository.save(block3);

        Progress progress = Progress.NOT_STARTED;
        Pageable pageable = PageRequest.of(0, 10);
        block1.statusUpdate();

        // when
        Page<Block> blocks = blockRepository.findByBlockWithProgress(progress, pageable);

        // then
        assertThat(blocks.getContent().size()).isEqualTo(1);
    }

}