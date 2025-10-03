package shop.kkeujeok.kkeujeokbackend.challenge.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.Type;
import shop.kkeujeok.kkeujeokbackend.block.domain.repository.BlockRepository;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeInfoResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Category;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Cycle;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.repository.ChallengeRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.repository.PersonalDashboardRepository;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.Role;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.notification.application.NotificationService;
import shop.kkeujeok.kkeujeokbackend.notification.util.SseEmitterManager;

@SpringBootTest
@Rollback(false)
@ActiveProfiles("test")
class ChallengeServiceConcurrencyTest {

    private static final int THREAD_COUNT = 100;

    @MockBean
    private NotificationService notificationService;
    @MockBean
    private SseEmitterManager sseEmitterManager;

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PersonalDashboardRepository personalDashboardRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Test
    void 챌린지_추가_동시성_테스트() throws InterruptedException {
        // given
        Member owner = createMember("owner@gmail.com", "동동");
        Challenge challenge = createChallenge(owner);
        PersonalDashboard dashboard = createDashboard(owner);
        createInitialBlock(owner, challenge, dashboard);

        Long challengeId = challenge.getId();
        Long dashboardId = dashboard.getId();

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            int idx = i;
            executorService.execute(() -> {
                try {
                    Member member = createMember("user" + idx + "@gmail.com", "닉네임" + idx);
                    challengeService.addChallengeToPersonalDashboard(member.getEmail(), challengeId, dashboardId);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(30, TimeUnit.SECONDS);

        // then
        assertThat(terminated)
                .withFailMessage("스레드 실행이 30초 내에 모두 종료되지 않았습니다.")
                .isTrue();

        ChallengeInfoResDto challengeInfo = challengeService.findById(owner.getEmail(), challengeId);
        assertThat(challengeInfo.participantCount()).isEqualTo(THREAD_COUNT);
    }

    private Member createMember(String email, String nickname) {
        return memberRepository.save(Member.builder()
                .status(Status.ACTIVE)
                .email(email)
                .name(nickname)
                .picture("기본 프로필")
                .socialType(SocialType.GOOGLE)
                .role(Role.ROLE_USER)
                .firstLogin(true)
                .nickname(nickname)
                .build());
    }

    private Challenge createChallenge(Member owner) {
        ChallengeSaveReqDto dto = new ChallengeSaveReqDto(
                "1일 1커밋",
                "1일 1커밋하기",
                Category.CREATIVITY_AND_ARTS,
                Cycle.WEEKLY,
                List.of(CycleDetail.MON, CycleDetail.TUE),
                LocalDate.now().plusDays(30),
                "1일 1커밋"
        );

        return challengeRepository.save(Challenge.builder()
                .title(dto.title())
                .contents(dto.contents())
                .cycle(dto.cycle())
                .cycleDetails(dto.cycleDetails())
                .startDate(LocalDate.now())
                .endDate(dto.endDate())
                .representImage("대표 이미지")
                .member(owner)
                .build());
    }

    private PersonalDashboard createDashboard(Member owner) {
        PersonalDashboardSaveReqDto dto = new PersonalDashboardSaveReqDto(
                "개인 대시보드",
                "테스트용 대시보드",
                false,
                "category"
        );

        return personalDashboardRepository.save(PersonalDashboard.builder()
                .title(dto.title())
                .description(dto.description())
                .isPublic(dto.isPublic())
                .category(dto.category())
                .member(owner)
                .build());
    }

    private void createInitialBlock(Member owner, Challenge challenge, PersonalDashboard dashboard) {
        blockRepository.save(Block.builder()
                .title(challenge.getTitle())
                .contents(challenge.getContents())
                .progress(Progress.NOT_STARTED)
                .type(Type.CHALLENGE)
                .deadLine(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd 23:59")))
                .member(owner)
                .dashboard(dashboard)
                .challenge(challenge)
                .build());
    }
}
