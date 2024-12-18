package shop.kkeujeok.kkeujeokbackend.challenge.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.Role;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;

class ChallengeTest {
    private Challenge challenge;
    private final String imageUrl = "대표 사진";

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .status(Status.ACTIVE)
                .email("kkeujeok@gmail.com")
                .name("김동균")
                .picture("프로필 사진")
                .socialType(SocialType.GOOGLE)
                .role(Role.ROLE_USER)
                .firstLogin(true)
                .nickname("동동")
                .build();

        challenge = Challenge.builder()
                .status(Status.ACTIVE)
                .title("제목")
                .contents("내용")
                .cycle(Cycle.WEEKLY)
                .cycleDetails(List.of(CycleDetail.MON, CycleDetail.TUE))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .representImage("대표 사진")
                .member(member)
                .blockName("블록 이름")
                .build();
    }

    @Test
    @DisplayName("챌린지의 모든 필드를 수정합니다.")
    void 챌린지_수정() {
        // given
        String updateTitle = "수정된 제목";
        String updateContents = "수정된 내용";
        Cycle updateCycle = Cycle.WEEKLY;
        List<CycleDetail> updateCycleDetails = List.of(CycleDetail.WED, CycleDetail.THU);
        LocalDate updateEndDate = LocalDate.now().plusDays(31);
        String updateRepresentImage = "수정된 대표 사진";
        String updateBlockName = "수정된 블록 이름";

        // when
        challenge.update(updateTitle, updateContents, updateCycle, updateCycleDetails, updateEndDate, updateBlockName,
                updateRepresentImage);

        // then
        assertAll(() -> {
            assertThat(challenge.getTitle()).isEqualTo(updateTitle);
            assertThat(challenge.getContents()).isEqualTo(updateContents);
            assertThat(challenge.getCycle()).isEqualTo(updateCycle);
            assertThat(challenge.getCycleDetails()).isEqualTo(updateCycleDetails);
            assertThat(challenge.getEndDate()).isEqualTo(updateEndDate);
            assertThat(challenge.getRepresentImage()).isEqualTo(updateRepresentImage);
            assertThat(challenge.getBlockName()).isEqualTo(updateBlockName);
            assertThat(challenge.getRepresentImage()).isEqualTo(updateRepresentImage);
        });
    }

    @Test
    @DisplayName("챌린지의 제목만 수정합니다.")
    void 챌린지_제목_수정() {
        // given
        String updateTitle = "수정된 제목";

        // when
        challenge.update(updateTitle, challenge.getContents(), challenge.getCycle(), challenge.getCycleDetails(),
                challenge.getEndDate(), challenge.getBlockName(), imageUrl);

        // then
        assertAll(() -> {
            assertThat(challenge.getTitle()).isEqualTo(updateTitle);
            assertThat(challenge.getContents()).isEqualTo("내용");
            assertThat(challenge.getCycleDetails()).isEqualTo(List.of(CycleDetail.MON, CycleDetail.TUE));
            assertThat(challenge.getEndDate()).isEqualTo(LocalDate.now().plusDays(30));
            assertThat(challenge.getRepresentImage()).isEqualTo("대표 사진");
            assertThat(challenge.getBlockName()).isEqualTo("블록 이름");
        });
    }

    @Test
    @DisplayName("챌린지의 내용만 수정합니다.")
    void 챌린지_내용_수정() {
        // given
        String updateContents = "수정된 내용";

        // when
        challenge.update(challenge.getTitle(), updateContents, challenge.getCycle(), challenge.getCycleDetails(),
                challenge.getEndDate(), challenge.getBlockName(), imageUrl);

        // then
        assertAll(() -> {
            assertThat(challenge.getTitle()).isEqualTo("제목");
            assertThat(challenge.getContents()).isEqualTo(updateContents);
            assertThat(challenge.getCycleDetails()).isEqualTo(List.of(CycleDetail.MON, CycleDetail.TUE));
            assertThat(challenge.getEndDate()).isEqualTo(LocalDate.now().plusDays(30));
            assertThat(challenge.getRepresentImage()).isEqualTo("대표 사진");
        });
    }

    @Test
    @DisplayName("챌린지의 마감일만 수정합니다.")
    void 챌린지_마감일_수정() {
        // given
        LocalDate updateEndDate = LocalDate.now().plusDays(40);

        // when
        challenge.update(challenge.getTitle(), challenge.getContents(), challenge.getCycle(),
                challenge.getCycleDetails(),
                updateEndDate, challenge.getBlockName(), imageUrl);

        // then
        assertAll(() -> {
            assertThat(challenge.getTitle()).isEqualTo("제목");
            assertThat(challenge.getContents()).isEqualTo("내용");
            assertThat(challenge.getCycleDetails()).isEqualTo(List.of(CycleDetail.MON, CycleDetail.TUE));
            assertThat(challenge.getEndDate()).isEqualTo(updateEndDate);
            assertThat(challenge.getRepresentImage()).isEqualTo("대표 사진");
        });
    }

    @Test
    @DisplayName("챌지의 상태를 수정합니다.")
    void 챌린지_상태_수정() {
        // given
        assertThat(challenge.getStatus()).isEqualTo(Status.ACTIVE);

        // when
        challenge.updateStatus();

        // then
        assertThat(challenge.getStatus()).isEqualTo(Status.DELETED);

        // when
        challenge.updateStatus();

        // then
        assertThat(challenge.getStatus()).isEqualTo(Status.ACTIVE);
    }
}
