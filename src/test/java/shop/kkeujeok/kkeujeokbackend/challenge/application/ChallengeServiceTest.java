package shop.kkeujeok.kkeujeokbackend.challenge.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.Type;
import shop.kkeujeok.kkeujeokbackend.block.domain.repository.BlockRepository;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSearchReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeInfoResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengesResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengesResDto.ChallengeSummary;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Category;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Cycle;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.repository.ChallengeRepository;
import shop.kkeujeok.kkeujeokbackend.challenge.exception.ChallengeAccessDeniedException;
import shop.kkeujeok.kkeujeokbackend.challenge.exception.InvalidCycleException;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.repository.PersonalDashboardRepository;
import shop.kkeujeok.kkeujeokbackend.global.aws.S3Service;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.Role;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.exception.MemberNotFoundException;
import shop.kkeujeok.kkeujeokbackend.notification.application.NotificationService;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceTest {

    private Member member;
    private Challenge challenge;
    private ChallengeSaveReqDto updateDto;
    private ChallengeSaveReqDto challengeSaveReqDto;
    private PersonalDashboard personalDashboard;
    private Block block;
    private PersonalDashboardSaveReqDto personalDashboardSaveReqDto;

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PersonalDashboardRepository personalDashboardRepository;

    @Mock
    private BlockRepository blockRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private ChallengeService challengeService;


    @BeforeEach
    void setUp() {
        member = Member.builder()
                .status(Status.ACTIVE)
                .email("kkeujeok@gmail.com")
                .name("김동균")
                .picture("기본 프로필")
                .socialType(SocialType.GOOGLE)
                .role(Role.ROLE_USER)
                .firstLogin(true)
                .nickname("동동")
                .build();

        lenient().when(memberRepository.findByEmail(anyString()))
                .thenReturn(Optional.ofNullable(member));

        challengeSaveReqDto = new ChallengeSaveReqDto(
                "1일 1커밋",
                "1일 1커밋하기",
                Category.CREATIVITY_AND_ARTS,
                Cycle.WEEKLY,
                List.of(CycleDetail.MON, CycleDetail.TUE),
                LocalDate.now().plusDays(30),
                "1일 1커밋"
        );

        challenge = Challenge.builder()
                .title(challengeSaveReqDto.title())
                .contents(challengeSaveReqDto.contents())
                .cycle(challengeSaveReqDto.cycle())
                .cycleDetails(challengeSaveReqDto.cycleDetails())
                .startDate(LocalDate.now())
                .endDate(challengeSaveReqDto.endDate())
                .representImage("대표 이미지")
                .member(member)
                .build();

        updateDto = new ChallengeSaveReqDto(
                "업데이트 제목",
                "업데이트 내용",
                Category.CREATIVITY_AND_ARTS,
                Cycle.WEEKLY,
                List.of(CycleDetail.MON),
                LocalDate.now().plusDays(30),
                "1일 1커밋"
        );

        block = Block.builder()
                .title(challenge.getTitle())
                .contents(challenge.getContents())
                .progress(Progress.NOT_STARTED)
                .type(Type.CHALLENGE)
                .deadLine(LocalDate.now()
                        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd 23:59")))
                .member(member)
                .dashboard(personalDashboard)
                .challenge(challenge)
                .build();

        personalDashboardSaveReqDto = new PersonalDashboardSaveReqDto("개인 대시보드",
                "테스트용 대시보드",
                false,
                "category");

        personalDashboard = PersonalDashboard.builder()
                .title(personalDashboardSaveReqDto.title())
                .description(personalDashboardSaveReqDto.description())
                .isPublic(personalDashboardSaveReqDto.isPublic())
                .category(personalDashboardSaveReqDto.category())
                .member(member)
                .build();
    }

    @Test
    @DisplayName("챌린지를 생성할 때 이미지를 업로드할 수 있다")
    void 챌린지를_생성할_때_이미지를_업로드할_수_있다() {
        // given
        String imageUrl = "https://example.com/image.jpg";
        when(s3Service.uploadChallengeImage(any(MultipartFile.class)))
                .thenReturn(imageUrl);

        // when
        ChallengeInfoResDto result = challengeService.save(member.getEmail(), challengeSaveReqDto, multipartFile);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("1일 1커밋");
            assertThat(result.contents()).isEqualTo("1일 1커밋하기");
            assertThat(result.cycleDetails()).isEqualTo(List.of(CycleDetail.MON, CycleDetail.TUE));
            assertThat(result.startDate()).isEqualTo(LocalDate.now());
            assertThat(result.endDate()).isEqualTo(LocalDate.now().plusDays(30));
            assertThat(result.authorName()).isEqualTo("동동");
            assertThat(result.authorProfileImage()).isEqualTo("기본 프로필");
            assertThat(result.representImage()).isEqualTo(imageUrl);
        });
    }

    @Test
    @DisplayName("이미지 업로드 없이 챌린지를 생성할 수 있다")
    void 이미지_업로드_없이_챌린지를_생성할_수_있다() {

        // given
        when(challengeRepository.save(any(Challenge.class)))
                .thenReturn(challenge);
        // when
        ChallengeInfoResDto result = challengeService.save(member.getEmail(), challengeSaveReqDto, multipartFile);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("1일 1커밋");
            assertThat(result.contents()).isEqualTo("1일 1커밋하기");
            assertThat(result.cycleDetails()).isEqualTo(List.of(CycleDetail.MON, CycleDetail.TUE));
            assertThat(result.startDate()).isEqualTo(LocalDate.now());
            assertThat(result.endDate()).isEqualTo(LocalDate.now().plusDays(30));
            assertThat(result.representImage()).isEqualTo(null);
            assertThat(result.authorName()).isEqualTo("동동");
            assertThat(result.authorProfileImage()).isEqualTo("기본 프로필");
        });
    }

    @Test
    @DisplayName("주기 정보와 세부 주기가 일치하지 않는다면 예외가 발생한다")
    void 주기_정보와_세부_주기가_일치하지_않는다면_예외가_발생한다() {
        // given
        ChallengeSaveReqDto wrongChallengeSaveReqDto = new ChallengeSaveReqDto(
                "1일 1커밋",
                "1일 1커밋하기",
                Category.CREATIVITY_AND_ARTS,
                Cycle.MONTHLY,
                List.of(CycleDetail.MON, CycleDetail.TUE),
                LocalDate.now(),
                "1일 1커밋"
        );

        // when & then
        assertThatThrownBy(
                () -> challengeService.save(member.getEmail(), wrongChallengeSaveReqDto, any(MultipartFile.class)))
                .isInstanceOf(InvalidCycleException.class);
    }

    @Test
    @DisplayName("회원 정보가 없다면 챌린지를 생성하려 하면 예외가 발생한다")
    void 회원_정보가_없다면_챌린지를_생성하려_하면_예외가_발생한다() {
        // given
        String errorEmail = "존재하지 않는 이메일@example.com";

        when(memberRepository.findByEmail(errorEmail))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> challengeService.save(errorEmail, challengeSaveReqDto, multipartFile))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("챌린지 작성자는 챌린지 정보를 업데이트할 수 있다")
    void 챌린지_작성자는_챌린지_정보를_업데이트할_수_있다() {
        // given
        Long challengeId = 1L;
        when(challengeRepository.findById(challengeId))
                .thenReturn(Optional.of(challenge));

        // when
        ChallengeInfoResDto result = challengeService.update(member.getEmail(), challengeId, updateDto, multipartFile);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("업데이트 제목");
            assertThat(result.contents()).isEqualTo("업데이트 내용");
            assertThat(result.cycleDetails()).containsExactly(CycleDetail.MON);
            assertThat(result.endDate()).isEqualTo(LocalDate.now().plusDays(30));
        });
    }

    @Test
    @DisplayName("챌린지 작성자가 아니면 수정 시 예외가 발생한다")
    void 챌린지_작성자가_아니면_수정_시_예외가_발생한다() {
        // given
        Long challengeId = 1L;
        Member otherMember = Member.builder()
                .status(Status.ACTIVE)
                .email("other@example.com")
                .name("다른 사용자")
                .picture("다른 프로필")
                .socialType(SocialType.GOOGLE)
                .role(Role.ROLE_USER)
                .firstLogin(true)
                .nickname("달라")
                .build();

        when(challengeRepository.findById(challengeId))
                .thenReturn(Optional.of(challenge));
        when(memberRepository.findByEmail("other@example.com"))
                .thenReturn(Optional.of(otherMember));

        // when & then
        assertThatThrownBy(
                () -> challengeService.update("other@example.com", challengeId, updateDto, any(MultipartFile.class)))
                .isInstanceOf(ChallengeAccessDeniedException.class);
    }

    @Test
    @DisplayName("모든 챌린지를 조회할 수 있다")
    void 모든_챌린지를_조회할_수_있다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        ChallengesResDto.ChallengeSummary summary = ChallengesResDto.ChallengeSummary.builder()
                .challengeId(1L)
                .representImage("image.png")
                .title("테스트 챌린지")
                .cycle(Cycle.DAILY)
                .cycleDetails(List.of())
                .createdAt(LocalDateTime.now())
                .build();

        List<ChallengesResDto.ChallengeSummary> summaries = List.of(summary);
        Page<ChallengesResDto.ChallengeSummary> page = new PageImpl<>(summaries, pageable, 1);

        when(challengeRepository.findAllChallenges(any(Pageable.class)))
                .thenReturn(page);

        // when
        ChallengesResDto result = challengeService.findAllChallenges(pageable);

        // then
        assertAll(() -> {
            assertThat(result.challenges().size()).isEqualTo(1);
            assertThat(result.pageInfoResDto().totalPages()).isEqualTo(1);
            assertThat(result.pageInfoResDto().totalItems()).isEqualTo(1);
            assertThat(result.challenges().get(0).title()).isEqualTo("테스트 챌린지");
        });
    }

    @Test
    @DisplayName("챌린지를 카테고리 별로 검색할 수 있다")
    void 챌린지를_카테고리_별로_검색할_수_있다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        ChallengeSearchReqDto searchReqDto = ChallengeSearchReqDto.from("1일", "CREATIVITY_AND_ARTS");

        ChallengeSummary summary = ChallengeSummary.builder()
                .challengeId(1L)
                .representImage("image.png")
                .title("테스트 챌린지")
                .cycle(Cycle.DAILY)
                .cycleDetails(List.of())
                .createdAt(LocalDateTime.now())
                .build();

        Page<ChallengeSummary> page = new PageImpl<>(List.of(summary), pageable, 1);

        when(challengeRepository.findChallengesByCategoryAndKeyword(any(ChallengeSearchReqDto.class),
                any(Pageable.class)))
                .thenReturn(page);

        // when
        ChallengesResDto result = challengeService.findChallengesByCategoryAndKeyword(searchReqDto, pageable);

        // then
        assertAll(
                () -> assertThat(result.challenges().size()).isEqualTo(1),
                () -> assertThat(result.pageInfoResDto().totalPages()).isEqualTo(1),
                () -> assertThat(result.pageInfoResDto().totalItems()).isEqualTo(1),
                () -> assertThat(result.challenges().get(0).title()).isEqualTo("테스트 챌린지"),
                () -> assertThat(result.challenges().get(0).cycle()).isEqualTo(Cycle.DAILY)
        );
    }

    @Test
    @DisplayName("챌린지 상세정보를 조회할 수 있다")
    void 챌린지_상세정보를_조회할_수_있다() {
        // given
        Long challengeId = 1L;
        when(challengeRepository.findById(challengeId)).thenReturn(Optional.of(challenge));

        // when
        ChallengeInfoResDto result = challengeService.findById(anyString(), challengeId);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("1일 1커밋");
            assertThat(result.contents()).isEqualTo("1일 1커밋하기");
            assertThat(result.cycleDetails()).isEqualTo(List.of(CycleDetail.MON, CycleDetail.TUE));
            assertThat(result.startDate()).isEqualTo(LocalDate.now());
            assertThat(result.endDate()).isEqualTo(LocalDate.now().plusDays(30));
            assertThat(result.representImage()).isEqualTo("대표 이미지");
            assertThat(result.authorName()).isEqualTo("동동");
            assertThat(result.authorProfileImage()).isEqualTo("기본 프로필");
        });
    }

    @Test
    @DisplayName("챌린지 작성자가 아니면 삭제할 수 없다")
    void 챌린지_작성자가_아니면_삭제할_수_없다() {
        // given
        Long challengeId = 1L;
        Member otherMember = Member.builder()
                .status(Status.ACTIVE)
                .email("other@example.com")
                .name("다른 사용자")
                .picture("다른 프로필")
                .socialType(SocialType.GOOGLE)
                .role(Role.ROLE_USER)
                .firstLogin(true)
                .nickname("달라")
                .build();
        when(challengeRepository.findById(challengeId))
                .thenReturn(Optional.of(challenge));
        when(memberRepository.findByEmail("other@example.com"))
                .thenReturn(Optional.of(otherMember));

        // when & then
        assertThatThrownBy(() -> challengeService.delete("other@example.com", challengeId))
                .isInstanceOf(ChallengeAccessDeniedException.class);
    }

    @Test
    @DisplayName("챌린지를 개인 대시보드에 추가할 수 있다")
    void 챌린지를_개인_대시보드에_추가할_수_있다() {
        // given
        Long personalDashboardId = 1L;
        Long challengeId = 1L;

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        when(challengeRepository.findById(anyLong())).thenReturn(Optional.of(challenge));
        when(personalDashboardRepository.findById(anyLong())).thenReturn(Optional.of(personalDashboard));

        // when
        challengeService.addChallengeToPersonalDashboard(member.getEmail(), personalDashboardId, challengeId);

        // then
        assertAll(() -> {
            assertThat(block.getTitle()).isEqualTo("1일 1커밋");
            assertThat(block.getContents()).isEqualTo("1일 1커밋하기");
            assertThat(block.getProgress()).isEqualTo(Progress.NOT_STARTED);
            assertThat(block.getDeadLine()).isEqualTo(
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd 23:59")));
        });
    }
}
