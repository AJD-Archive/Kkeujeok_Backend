package shop.kkeujeok.kkeujeokbackend.challenge.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import shop.kkeujeok.kkeujeokbackend.auth.exception.EmailNotFoundException;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeInfoResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Cycle;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.repository.ChallengeRepository;
import shop.kkeujeok.kkeujeokbackend.challenge.exception.InvalidCycleException;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.Role;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;

class ChallengeServiceTest {

    private ChallengeSaveReqDto challengeSaveReqDto;
    private LocalDate startDate;
    private LocalDate endDate;

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ChallengeService challengeService;

    private Member member;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setUpMember();
        setUpMocks();
        startDate = LocalDate.now();
        endDate = startDate.plusDays(30);
    }

    private void setUpMember() {
        member = Member.builder()
                .status(Status.A)
                .email("kkeujeok@gmail.com")
                .name("김동균")
                .picture("기본 프로필")
                .socialType(SocialType.GOOGLE)
                .role(Role.ROLE_USER)
                .firstLogin(true)
                .nickname("동동")
                .build();
    }

    private void setUpMocks() {
        when(memberRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(member));
        when(challengeRepository.save(any(Challenge.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    @DisplayName("로그인된 회원은 챌린지를 생성할 수 있다")
    void 로그인_된_회원은_챌린지를_생성할_수_있다() {
        // given
        setChallengeSaveReqDto(Cycle.WEEKLY, List.of(CycleDetail.MON, CycleDetail.TUE));

        // when
        ChallengeInfoResDto result = challengeService.create(member.getEmail(), challengeSaveReqDto);

        // then
        assertChallengeInfo(result, List.of(CycleDetail.MON, CycleDetail.TUE));
    }

    @Test
    @DisplayName("주기 정보와 세부 주기가 일치하지 않는다면 예외가 발생한다")
    void 주기_정보와_세부_주기가_일치하지_않는다면_예외가_발생한다() {
        // given
        setChallengeSaveReqDto(Cycle.MONTHLY, List.of(CycleDetail.MON, CycleDetail.TUE));

        // when & then
        assertThatThrownBy(() -> challengeService.create(member.getEmail(), challengeSaveReqDto))
                .isInstanceOf(InvalidCycleException.class);
    }

    @Test
    @DisplayName("회원 정보가 없다면 챌린지를 생성하려 하면 예외가 발생한다")
    void 회원_정보가_없다면_챌린지를_생성하려_하면_예외가_발생한다() {
        // given
        String email = "nonexistent@example.com";
        setChallengeSaveReqDto(Cycle.WEEKLY, List.of(CycleDetail.MON, CycleDetail.TUE));
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> challengeService.create(email, challengeSaveReqDto))
                .isInstanceOf(EmailNotFoundException.class);
    }

    private void setChallengeSaveReqDto(Cycle cycle, List<CycleDetail> cycleDetails) {
        challengeSaveReqDto = new ChallengeSaveReqDto(
                "1일 1커밋",
                "1일 1커밋하기",
                cycle,
                cycleDetails,
                startDate.toString(),
                endDate.toString(),
                "대표 이미지"
        );
    }

    private void assertChallengeInfo(ChallengeInfoResDto result, List<CycleDetail> cycleDetails) {
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("1일 1커밋");
            assertThat(result.contents()).isEqualTo("1일 1커밋하기");
            assertThat(result.cycleDetails()).isEqualTo(cycleDetails);
            assertThat(result.startDate()).isEqualTo(startDate.toString());
            assertThat(result.endDate()).isEqualTo(endDate.toString());
            assertThat(result.representImage()).isEqualTo("대표 이미지");
            assertThat(result.authorName()).isEqualTo("동동");
            assertThat(result.authorProfileImage()).isEqualTo("기본 프로필");
        });
    }
}
