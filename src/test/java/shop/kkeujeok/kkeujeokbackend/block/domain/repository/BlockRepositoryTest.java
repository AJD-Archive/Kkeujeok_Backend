package shop.kkeujeok.kkeujeokbackend.block.domain.repository;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
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
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.Dashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.repository.DashboardRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.global.config.JpaAuditingConfig;
import shop.kkeujeok.kkeujeokbackend.global.config.QuerydslConfig;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;

@DataJpaTest
@Import({JpaAuditingConfig.class, QuerydslConfig.class})
@ActiveProfiles("test")
class BlockRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private DashboardRepository dashboardRepository;

    private Member member;
    private Dashboard dashboard;
    private Block block1;
    private Block block2;
    private Block block3;


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

        dashboard = PersonalDashboard.builder()
                .member(member)
                .title("title")
                .description("description")
                .isPublic(false)
                .category("category")
                .build();

        block1 = Block.builder()
                .title("title1")
                .contents("contents1")
                .progress(Progress.NOT_STARTED)
                .deadLine("2024.07.27 11:03")
                .dashboard(dashboard)
                .sequence(1)
                .build();

        block2 = Block.builder()
                .title("title2")
                .contents("contents2")
                .progress(Progress.NOT_STARTED)
                .deadLine("2024.07.27 11:04")
                .dashboard(dashboard)
                .sequence(2)
                .build();

        block3 = Block.builder()
                .title("title3")
                .contents("contents3")
                .progress(Progress.IN_PROGRESS)
                .deadLine("2024.07.27 11:05")
                .dashboard(dashboard)
                .sequence(3)
                .build();

        memberRepository.save(member);
        dashboardRepository.save(dashboard);
        blockRepository.save(block1);
        blockRepository.save(block2);
        blockRepository.save(block3);
    }

    @DisplayName("블록을 진행 상태별로 전체 조회합니다.")
    @Test
    void 블록_진행_상태_전체_조회() {
        // given
        Progress progress = Progress.NOT_STARTED;
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Block> blocks = blockRepository.findByBlockWithProgress(dashboard.getId(), progress, pageable);

        // then
        assertThat(blocks.getContent().size()).isEqualTo(2);
    }

    @DisplayName("블록을 논리 삭제 상태별로 전체 조회합니다.")
    @Test
    void 블록_삭제_상태_전체_조회() {
        // given
        Progress progress = Progress.NOT_STARTED;
        Pageable pageable = PageRequest.of(0, 10);
        block1.statusUpdate();

        // when
        Page<Block> blocks = blockRepository.findByBlockWithProgress(1L, progress, pageable);

        // then
        assertThat(blocks.getContent().size()).isEqualTo(1);
    }

    @DisplayName("블록의 마지막 순번을 가져옵니다.")
    @Test
    void 블록_마지막_순번() {
        // given

        // when
        int lastSequence = blockRepository.findLastSequenceByProgress(member, dashboard.getId(), Progress.NOT_STARTED);

        // then
        assertThat(lastSequence).isEqualTo(2);
    }

}