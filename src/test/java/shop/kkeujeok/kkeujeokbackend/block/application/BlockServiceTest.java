package shop.kkeujeok.kkeujeokbackend.block.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.repository.BlockRepository;

@ExtendWith(MockitoExtension.class)
class BlockServiceTest {

    @Mock
    private BlockRepository blockRepository;

    @InjectMocks
    private BlockService blockService;

    private Block block;
    private BlockSaveReqDto blockSaveReqDto;

    @BeforeEach
    void setUp() {
        blockSaveReqDto = new BlockSaveReqDto("Title", "Contents", Progress.NOT_STARTED);
        block = Block.builder()
                .title(blockSaveReqDto.title())
                .contents(blockSaveReqDto.contents())
                .progress(blockSaveReqDto.progress())
                .build();
    }

    @DisplayName("블록을 저장합니다.")
    @Test
    void 블록_저장() {
        // given
        when(blockRepository.save(any(Block.class))).thenReturn(block);

        // when
        BlockInfoResDto result = blockService.save(blockSaveReqDto);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("Title");
            assertThat(result.contents()).isEqualTo("Contents");
            assertThat(result.progress()).isEqualTo(Progress.NOT_STARTED);
            assertNotNull(result.nickname());
        });

    }

}