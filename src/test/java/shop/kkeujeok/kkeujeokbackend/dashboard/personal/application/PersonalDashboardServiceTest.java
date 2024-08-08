package shop.kkeujeok.kkeujeokbackend.dashboard.personal.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.repository.PersonalDashboardRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.exception.DashboardAccessDeniedException;
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
        personalDashboardUpdateReqDto = new PersonalDashboardUpdateReqDto("updateTitle", "updateDescription",
                "updateCategory");
        personalDashboard = PersonalDashboard.builder()
                .title(personalDashboardSaveReqDto.title())
                .description(personalDashboardSaveReqDto.description())
                .isPublic(personalDashboardSaveReqDto.isPublic())
                .category(personalDashboardSaveReqDto.category())
                .member(member)
                .build();
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
            assertThat(result.isPublic()).isEqualTo(false);
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
                () -> personalDashboardService.update(unauthorizedEmail, dashboardId, personalDashboardUpdateReqDto))
                .isInstanceOf(DashboardAccessDeniedException.class);
    }

}