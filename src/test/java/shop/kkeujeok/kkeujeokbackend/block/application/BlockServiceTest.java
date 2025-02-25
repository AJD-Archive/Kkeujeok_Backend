package shop.kkeujeok.kkeujeokbackend.block.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockSequenceUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.repository.BlockRepository;
import shop.kkeujeok.kkeujeokbackend.block.exception.InvalidProgressException;
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.Dashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.repository.DashboardRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class BlockServiceTest {

    @Mock
    private BlockRepository blockRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private DashboardRepository dashboardRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @InjectMocks
    private BlockService blockService;

    private Member member;
    private Block block;
    private Block deleteBlock;
    private Dashboard dashboard;
    private BlockSaveReqDto blockSaveReqDto;
    private BlockUpdateReqDto blockUpdateReqDto;
    private BlockSequenceUpdateReqDto blockSequenceUpdateReqDto;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email("email")
                .name("name")
                .nickname("nickname")
                .socialType(SocialType.GOOGLE)
                .introduction("introduction")
                .picture("picture")
                .build();

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(member));

        dashboard = PersonalDashboard.builder()
                .member(member)
                .title("title")
                .description("description")
                .isPublic(false)
                .category("category")
                .build();

        blockSaveReqDto = new BlockSaveReqDto(1L, "Title", "Contents", Progress.NOT_STARTED, "2024.07.03 13:23",
                "2024.07.25 13:23");
        blockUpdateReqDto = new BlockUpdateReqDto("UpdateTitle", "UpdateContents", "2024.07.03 13:23",
                "2024.07.28 16:40");

        blockSequenceUpdateReqDto = new BlockSequenceUpdateReqDto(
                dashboard.getId(),
                List.of(1L),
                List.of(),
                List.of()
        );

        block = Block.builder()
                .title(blockSaveReqDto.title())
                .contents(blockSaveReqDto.contents())
                .progress(blockSaveReqDto.progress())
                .startDate(blockSaveReqDto.startDate())
                .deadLine(blockSaveReqDto.deadLine())
                .member(member)
                .dashboard(dashboard)
                .build();

        deleteBlock = Block.builder()
                .title(blockSaveReqDto.title())
                .contents(blockSaveReqDto.contents())
                .progress(blockSaveReqDto.progress())
                .startDate(blockSaveReqDto.startDate())
                .deadLine(blockSaveReqDto.deadLine())
                .member(member)
                .dashboard(dashboard)
                .build();
        deleteBlock.statusUpdate();
    }

    @DisplayName("블록을 저장합니다.")
    @Test
    void 블록_저장() {
        // given
        when(blockRepository.save(any(Block.class))).thenReturn(block);
        when(dashboardRepository.findById(anyLong())).thenReturn(Optional.of(dashboard));
        when(redisTemplate.opsForValue()).thenReturn(mock(ValueOperations.class));

        // when
        BlockInfoResDto result = blockService.save("email", blockSaveReqDto);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("Title");
            assertThat(result.contents()).isEqualTo("Contents");
            assertThat(result.progress()).isEqualTo(Progress.NOT_STARTED);
            assertThat(result.deadLine()).isEqualTo("2024.07.25 13:23");
            assertNotNull(result.nickname());
        });
    }

    @DisplayName("블록을 수정합니다.")
    @Test
    void 블록_수정() {
        // given
        Long blockId = 1L;
        when(blockRepository.findById(blockId)).thenReturn(Optional.of(block));
        when(dashboardRepository.findById(block.getDashboard().getId())).thenReturn(Optional.of(dashboard));

        // when
        BlockInfoResDto result = blockService.update("email", blockId, blockUpdateReqDto);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("UpdateTitle");
            assertThat(result.contents()).isEqualTo("UpdateContents");
            assertThat(result.deadLine()).isEqualTo("2024.07.28 16:40");
        });
    }

    @DisplayName("블록 제목과 내용이 기존과 동일하면 수정 하지 않습니다.")
    @Test
    void 블록_수정_X() {
        // given
        Long blockId = 1L;
        BlockUpdateReqDto originBlockUpdateReqDto = new BlockUpdateReqDto("Title", "Contents", "2024.07.03 13:23",
                "2024.07.25 13:23");
        when(blockRepository.findById(blockId)).thenReturn(Optional.of(block));
        when(dashboardRepository.findById(block.getDashboard().getId())).thenReturn(Optional.of(dashboard));

        // when
        BlockInfoResDto result = blockService.update("email", blockId, originBlockUpdateReqDto);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("Title");
            assertThat(result.contents()).isEqualTo("Contents");
            assertThat(block.getUpdatedAt()).isNull();
        });
    }

    @DisplayName("블록의 상태를 수정합니다.")
    @Test
    void 블록_상태_수정() {
        // given
        Long blockId = 1L;
        when(blockRepository.findById(blockId)).thenReturn(Optional.of(block));
        when(dashboardRepository.findById(block.getDashboard().getId())).thenReturn(Optional.of(dashboard));

        // when
        BlockInfoResDto result = blockService.progressUpdate("email",
                blockId,
                "IN_PROGRESS",
                blockSequenceUpdateReqDto);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("Title");
            assertThat(result.contents()).isEqualTo("Contents");
            assertThat(result.progress()).isEqualTo(Progress.IN_PROGRESS);
        });
    }

    @DisplayName("블록의 상태를 수정하는데 실패합니다.(Progress 문자열 파싱 실패)")
    @Test
    void 블록_상태_수정_실패() {
        // given
        Long blockId = 1L;
        when(blockRepository.findById(blockId)).thenReturn(Optional.of(block));

        // when & then
        assertThatThrownBy(() -> blockService.progressUpdate("email", blockId, "String", blockSequenceUpdateReqDto))
                .isInstanceOf(InvalidProgressException.class);
    }

    @DisplayName("블록을 삭제 합니다.")
    @Test
    void 블록_삭제() {
        // given
        Long blockId = 1L;
        when(blockRepository.findById(blockId)).thenReturn(Optional.of(block));
        when(dashboardRepository.findById(block.getDashboard().getId())).thenReturn(Optional.of(dashboard));

        // when
        blockService.delete("email", blockId);

        // then
        assertAll(() -> {
            assertThat(block.getStatus()).isEqualTo(Status.DELETED);
        });
    }

    @DisplayName("삭제되었던 블록을 복구합니다.")
    @Test
    void 블록_복구() {
        // given
        Long blockId = 1L;
        when(blockRepository.findById(blockId)).thenReturn(Optional.of(deleteBlock));
        when(dashboardRepository.findById(block.getDashboard().getId())).thenReturn(Optional.of(dashboard));

        // when
        blockService.delete("email", blockId);

        // then
        assertAll(() -> {
            assertThat(deleteBlock.getStatus()).isEqualTo(Status.ACTIVE);
        });
    }

    @DisplayName("블록을 상세 봅니다.")
    @Test
    void 블록_상세보기() {
        // given
        Long blockId = 1L;
        when(blockRepository.findById(blockId)).thenReturn(Optional.of(block));

        // when
        BlockInfoResDto result = blockService.findById("email", blockId);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("Title");
            assertThat(result.contents()).isEqualTo("Contents");
            assertThat(result.progress()).isEqualTo(Progress.NOT_STARTED);
            assertThat(result.deadLine()).isEqualTo("2024.07.25 13:23");
            assertNotNull(result.nickname());
        });
    }

    @DisplayName("블록의 순번을 변경합니다.")
    @Test
    void 블록_순번_변경() {
        // given
        when(blockRepository.findById(anyLong())).thenReturn(Optional.of(block));
        when(dashboardRepository.findById(block.getDashboard().getId())).thenReturn(Optional.of(dashboard));

        // when
        blockService.changeBlocksSequence("email", blockSequenceUpdateReqDto);

        // then
        verify(blockRepository, times(1)).findById(anyLong());  // 6번 블록 조회가 이루어졌는지 검증
    }

    @DisplayName("블록을 영구 삭제 합니다.")
    @Test
    void 블록_영구_삭제() {
        // given
        Long blockId = 1L;
        when(blockRepository.findById(blockId)).thenReturn(Optional.of(deleteBlock));
        when(dashboardRepository.findById(block.getDashboard().getId())).thenReturn(Optional.of(dashboard));

        // when
        blockService.deletePermanently("email", blockId);

        // then
        verify(blockRepository, times(1)).delete(deleteBlock);
    }

    @DisplayName("삭제된 블록을 전체 삭제합니다.")
    @Test
    void 삭제_블록_전체_삭제() {
        // given
        Long dashboardId = 1L;
        List<Block> deletedBlocks = List.of(deleteBlock);
        when(blockRepository.findByDeletedBlocks(dashboardId)).thenReturn(deletedBlocks);
        when(dashboardRepository.findById(dashboardId)).thenReturn(Optional.of(dashboard));

        // when
        blockService.deleteAllPermanently("email", dashboardId);

        // then
        verify(blockRepository).deleteAll(deletedBlocks);
    }

}