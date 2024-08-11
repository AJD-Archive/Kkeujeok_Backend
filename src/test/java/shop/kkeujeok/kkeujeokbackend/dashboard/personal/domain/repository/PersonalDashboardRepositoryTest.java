package shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

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
import shop.kkeujeok.kkeujeokbackend.block.domain.repository.BlockRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.global.config.JpaAuditingConfig;
import shop.kkeujeok.kkeujeokbackend.global.config.QuerydslConfig;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;

@DataJpaTest
@Import({JpaAuditingConfig.class, QuerydslConfig.class})
@ActiveProfiles("test")
class PersonalDashboardRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private PersonalDashboardRepository personalDashboardRepository;

    private Member member;
    private PersonalDashboard personalDashboard;
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

        memberRepository.save(member);

        personalDashboard = PersonalDashboard.builder()
                .member(member)
                .title("title")
                .description("description")
                .isPublic(false)
                .category("category")
                .build();

        personalDashboardRepository.save(personalDashboard);

        block1 = Block.builder()
                .title("title1")
                .contents("contents1")
                .progress(Progress.COMPLETED)
                .deadLine("2024.07.27 11:03")
                .dashboard(personalDashboard)
                .build();

        block2 = Block.builder()
                .title("title2")
                .contents("contents2")
                .progress(Progress.NOT_STARTED)
                .deadLine("2024.07.27 11:04")
                .dashboard(personalDashboard)
                .build();

        block3 = Block.builder()
                .title("title3")
                .contents("contents3")
                .progress(Progress.COMPLETED)
                .deadLine("2024.07.27 11:05")
                .dashboard(personalDashboard)
                .build();

        blockRepository.save(block1);
        blockRepository.save(block2);
        blockRepository.save(block3);
    }

    @DisplayName("개인 대시보드를 전체 조회합니다.")
    @Test
    void 개인_대시보드_전체_조회() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<PersonalDashboard> result = personalDashboardRepository.findForPersonalDashboard(member, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("title");
        assertThat(result.getContent().get(0).getMember()).isEqualTo(member);
    }

    @DisplayName("대시보드의 완료된 블록 비율을 계산합니다.")
    @Test
    void 개인_대시보드_완료_블록_비율_계산() {
        // when
        double completionPercentage = personalDashboardRepository
                .calculateCompletionPercentage(personalDashboard.getId());

        // then
        assertThat(completionPercentage).isEqualTo(66.67, offset(0.01));
    }

}