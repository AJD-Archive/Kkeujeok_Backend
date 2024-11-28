인호
inhooo00
온라인
김신아 — 2024. 10. 9. 오후 11:25
쿨쿨띠얘
쿨쿨
쿨쿨띠예
드르렁 드르렁 드르렁 대~~
명지우 — 2024. 10. 9. 오후 11:57
알고보니 혼잣말이었던거임
~
김신아 — 2024. 10. 10. 오전 12:03
챌린지 매니절~
쉿쉿
자잔~
흑흐ㄱ
흐흫ㄱㄱ흑ㄱ흐흑.. 흑긓흐ㅡㄱ흐흑 흐ㅡ흑
일단 알겟음 개발 끝나면 console 지욹레
지울게
김신아 — 2024. 10. 10. 오전 12:18
애들아 나빨래 널고싶은데
나 나가도 돼..?
나 사실 빨래 2시간동안 묵혀두고잇어
...................................................
허락해주세요 주인님 흑흑
김동균 — 2024. 10. 10. 오전 12:18
가세요
김신아 — 2024. 10. 10. 오전 12:18
빨래 널러 갈 수 있게 ㅎ락해주세요
아싸라비용
최기웅 — 2024. 10. 10. 오전 12:20
ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ
명지우 — 2024. 10. 10. 오전 12:21
빠이빠이
김신아 — 오늘 오후 10:00
ㅋㅋ ㅎㅇ
ㅇㅇ!!
예스~~
오우
헐 머임
명지우 — 오늘 오후 10:02
근데 언니 어디야??
김신아 — 오늘 오후 10:03
나 지금
지하철 ㅜ
나
친구 생일파티라
합창연습
하느라 조금 늦었네…
ㅇㅇ 내가 소프라노임
☺️
오늘 영상찍음 ㅋ
ㅇㅋ
뭐임
동균이 ㅋㅋㅋㅋㅋ
이거
누가짠겨?
전에!
?
김동균 — 오늘 오후 10:07
ㄱㅇ
김신아 — 오늘 오후 10:07
ㅇㅎ
얼른 배포해보자긔
동의합니다
야호~
재채기누구임
오 수고수고
나도 다함
근데
그 sse가 됐다 말앗다해사
동균이가 http버전2
사용하면
탭6개로 제한된대
ㅋㅋ
ㅇㅇ
흐엉웅
흐엉엉
애들아
내가 방금 코드 잘못고친거 생각남
지금 아예 알림 인뜰더임 ㅋㅋ
ㅈㅅ
집가서 고침
김동균 — 오늘 오후 10:13
ㅇㅋ ㅋㅋ
인호 — 오늘 오후 10:13
개웃기네 ㅋㅋ
김신아 — 오늘 오후 10:14
응응
어디간격??
나 간격 줄엿는데 별로여서 늘린건데..하핫
다시 늘맇게용
꺅
ㅇㅇ
내일 발표인데
ㅅㅂ
아무것도 못랫ㅅ아
흑흣흑흫흑흐흑
🥹
인호 — 오늘 오후 10:16
이미지
김신아 — 오늘 오후 10:18
야호~~
ㅋㅋㅋㅋㄱㅋㅋㄱㅋㄱㄱㅋㅋㄱㅋㄱㅋㄱㅋ
큭큭
학겨가 엏른 돈줫으면
배포댐 !
??
ㅇㅋ..
나 15분뒤에내림
헤헤
아냐
그 뒤에 오류나는 거
내가 맡은거에서
오루나면
내가 달려가기로 했어
야호~~
ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ
👍
응애
김동균 — 오늘 오후 10:29
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
                List.of(1L, 2L),
                List.of(3L, 4L),
                List.of(5L, 6L)
        );

... (214줄 남음)
접기
message.txt
12KB
﻿
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
                List.of(1L, 2L),
                List.of(3L, 4L),
                List.of(5L, 6L)
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
        BlockInfoResDto result = blockService.progressUpdate("email", blockId, "IN_PROGRESS");

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
        assertThatThrownBy(() -> blockService.progressUpdate("email", blockId, "String"))
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
        verify(blockRepository, times(6)).findById(anyLong());  // 6번 블록 조회가 이루어졌는지 검증
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
message.txt
12KB