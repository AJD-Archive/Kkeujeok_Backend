package shop.kkeujeok.kkeujeokbackend.dashboard.personal.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardCategoriesResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.repository.PersonalDashboardRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.exception.DashboardAccessDeniedException;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class PersonalDashboardServiceTest {

    @Mock
    private PersonalDashboardRepository personalDashboardRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private PersonalDashboardService personalDashboardService;

    private Member member;
    private PersonalDashboard personalDashboard;
    private PersonalDashboard deletePersonalDashboard;
    private PersonalDashboardSaveReqDto personalDashboardSaveReqDto;
    private PersonalDashboardUpdateReqDto personalDashboardUpdateReqDto;

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

        personalDashboardSaveReqDto = new PersonalDashboardSaveReqDto("title", "description", false, "category");
        personalDashboardUpdateReqDto = new PersonalDashboardUpdateReqDto(
                "updateTitle",
                "updateDescription",
                true,
                "updateCategory");

        personalDashboard = PersonalDashboard.builder()
                .title(personalDashboardSaveReqDto.title())
                .description(personalDashboardSaveReqDto.description())
                .isPublic(personalDashboardSaveReqDto.isPublic())
                .category(personalDashboardSaveReqDto.category())
                .member(member)
                .build();

        deletePersonalDashboard = PersonalDashboard.builder()
                .title(personalDashboardSaveReqDto.title())
                .description(personalDashboardSaveReqDto.description())
                .isPublic(personalDashboardSaveReqDto.isPublic())
                .category(personalDashboardSaveReqDto.category())
                .member(member)
                .build();
        deletePersonalDashboard.statusUpdate();
    }

    @DisplayName("개인 대시보드를 저장합니다.")
    @Test
    void 개인_대시보드_저장() {
        // given
        when(personalDashboardRepository.save(any(PersonalDashboard.class))).thenReturn(personalDashboard);

        // when
        PersonalDashboardInfoResDto result = personalDashboardService.save(
                member.getEmail(),
                personalDashboardSaveReqDto);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("title");
            assertThat(result.description()).isEqualTo("description");
            assertThat(result.isPublic()).isEqualTo(false);
            assertThat(result.category()).isEqualTo("category");
        });
    }

    @DisplayName("개인 대시보드를 수정합니다.")
    @Test
    void 개인_대시보드_수정() {
        // given
        Long dashboardId = 1L;
        when(personalDashboardRepository.findById(dashboardId)).thenReturn(Optional.of(personalDashboard));

        // when
        PersonalDashboardInfoResDto result = personalDashboardService.update(
                member.getEmail(),
                dashboardId,
                personalDashboardUpdateReqDto);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("updateTitle");
            assertThat(result.description()).isEqualTo("updateDescription");
            assertThat(result.isPublic()).isEqualTo(true);
            assertThat(result.category()).isEqualTo("updateCategory");
        });
    }

    @DisplayName("생성자가 아닌 사용자가 개인 대시보드를 수정하는데 실패합니다. (DashboardAccessDeniedException 발생)")
    @Test
    void 개인_대시보드_수정_실패() {
        // given
        Long dashboardId = 1L;
        String unauthorizedEmail = "unauthorizedEmail@email.com";

        Member unauthorizedMember = Member.builder()
                .email(unauthorizedEmail)
                .name("name")
                .nickname("nickname")
                .socialType(SocialType.GOOGLE)
                .introduction("introduction")
                .picture("picture")
                .build();

        when(memberRepository.findByEmail(unauthorizedEmail)).thenReturn(Optional.of(unauthorizedMember));
        when(personalDashboardRepository.findById(dashboardId)).thenReturn(Optional.of(personalDashboard));

        // when & then
        assertThatThrownBy(
                () -> personalDashboardService.update(unauthorizedEmail, dashboardId, personalDashboardUpdateReqDto)
        ).isInstanceOf(DashboardAccessDeniedException.class);
    }

    @DisplayName("개인 대시보드를 삭제합니다.")
    @Test
    void 개인_대시보드_삭제() {
        // given
        Long dashboardId = 1L;
        when(personalDashboardRepository.findById(dashboardId)).thenReturn(Optional.of(personalDashboard));

        // when
        personalDashboardService.delete(member.getEmail(), dashboardId);

        // then
        assertAll(() -> {
            assertThat(personalDashboard.getStatus()).isEqualTo(Status.DELETED);
        });
    }

    @DisplayName("개인 대시보드를 전체 조회합니다.")
    @Test
    void 개인_대시보드_전체_조회() {
        // given
        when(personalDashboardRepository.findForPersonalDashboard(any(Member.class)))
                .thenReturn(List.of(personalDashboard));

        // when
        PersonalDashboardListResDto result = personalDashboardService.findForPersonalDashboard(member.getEmail());

        // then
        assertThat(result).isNotNull();
        assertThat(result.personalDashboardListResDto()).hasSize(1);
    }

    @DisplayName("개인 대시보드를 상세 봅니다.")
    @Test
    void 개인_대시보드_상세보기() {
        // given
        Long dashboardId = 1L;
        when(personalDashboardRepository.findById(dashboardId)).thenReturn(Optional.of(personalDashboard));

        // when
        PersonalDashboardInfoResDto result = personalDashboardService.findById(member.getEmail(), dashboardId);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("title");
            assertThat(result.description()).isEqualTo("description");
            assertThat(result.isPublic()).isEqualTo(false);
            assertThat(result.category()).isEqualTo("category");
            assertThat(result.blockProgress()).isEqualTo(0.0);
        });
    }

    @DisplayName("개인 대시보드 카테고리를 조회합니다.")
    @Test
    void 개인_대시보드_카테고리_조회() {
        // given
        List<String> categories = List.of("category");
        when(personalDashboardRepository.findForPersonalDashboardByCategory(any(Member.class))).thenReturn(categories);

        // when
        PersonalDashboardCategoriesResDto result = personalDashboardService.findForPersonalDashboardByCategories(
                member.getEmail());

        // then
        assertThat(result.categories()).hasSize(1);
        assertThat(result.categories().get(0)).isEqualTo("category");
    }

    @DisplayName("삭제되었던 개인 대시보드를 복구합니다.")
    @Test
    void 개인_대시보드_복구() {
        // given
        Long dashboardId = 1L;
        when(personalDashboardRepository.findById(dashboardId)).thenReturn(Optional.of(deletePersonalDashboard));

        // when
        personalDashboardService.delete(member.getEmail(), dashboardId);

        // then
        assertAll(() -> {
            assertThat(deletePersonalDashboard.getStatus()).isEqualTo(Status.ACTIVE);
        });
    }

}