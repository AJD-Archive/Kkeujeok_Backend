package shop.kkeujeok.kkeujeokbackend.dashboard.team.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.exception.DashboardAccessDeniedException;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request.TeamDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request.TeamDashboardUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.SearchMemberListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.repository.TeamDashboardRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.exception.AlreadyJoinedTeamException;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.exception.NotMemberOfTeamException;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.notification.application.NotificationService;

@ExtendWith(MockitoExtension.class)
class TeamDashboardServiceTest {

    @Mock
    private TeamDashboardRepository teamDashboardRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TeamDashboardService teamDashboardService;

    private Member member;
    private TeamDashboard teamDashboard;
    private TeamDashboard deleteTeamDashboard;
    private TeamDashboardSaveReqDto teamDashboardSaveReqDto;
    private TeamDashboardUpdateReqDto teamDashboardUpdateReqDto;

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

        lenient().when(memberRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(member));

        teamDashboardSaveReqDto = new TeamDashboardSaveReqDto("title", "description", List.of("joinEmail"));
        teamDashboardUpdateReqDto = new TeamDashboardUpdateReqDto("updateTitle", "updateDescription",
                List.of("joinEmail"));

        teamDashboard = TeamDashboard.builder()
                .title(teamDashboardSaveReqDto.title())
                .description(teamDashboardSaveReqDto.description())
                .member(member)
                .build();

        deleteTeamDashboard = TeamDashboard.builder()
                .title(teamDashboardSaveReqDto.title())
                .description(teamDashboardSaveReqDto.description())
                .member(member)
                .build();
        deleteTeamDashboard.statusUpdate();
    }

    @DisplayName("팀 대시보드를 저장합니다.")
    @Test
    void 팀_대시보드_저장() {
        // given
        when(teamDashboardRepository.save(any(TeamDashboard.class))).thenReturn(teamDashboard);

        // when
        TeamDashboardInfoResDto result = teamDashboardService.save(member.getEmail(), teamDashboardSaveReqDto);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("title");
            assertThat(result.description()).isEqualTo("description");
        });
    }

    @DisplayName("팀 대시보드를 수정합니다.")
    @Test
    void 팀_대시보드_수정() {
        // given
        Long dashboardId = 1L;
        when(teamDashboardRepository.findById(dashboardId)).thenReturn(Optional.of(teamDashboard));

        // when
        TeamDashboardInfoResDto result = teamDashboardService.update(
                member.getEmail(),
                dashboardId,
                teamDashboardUpdateReqDto);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("updateTitle");
            assertThat(result.description()).isEqualTo("updateDescription");
        });
    }

    @DisplayName("생성자가 아닌 사용자가 팀 대시보드를 수정하는데 실패합니다. (DashboardAccessDeniedException 발생)")
    @Test
    void 팀_대시보드_수정_실패() {
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
        when(teamDashboardRepository.findById(dashboardId)).thenReturn(Optional.of(teamDashboard));

        // when & then
        assertThatThrownBy(
                () -> teamDashboardService.update(unauthorizedEmail, dashboardId, teamDashboardUpdateReqDto)
        ).isInstanceOf(DashboardAccessDeniedException.class);
    }

    @DisplayName("팀 대시보드를 삭제합니다.")
    @Test
    void 팀_대시보드_삭제() {
        // given
        Long dashboardId = 1L;
        when(teamDashboardRepository.findById(dashboardId)).thenReturn(Optional.of(teamDashboard));

        // when
        teamDashboardService.delete(member.getEmail(), dashboardId);

        // then
        assertAll(() -> {
            assertThat(teamDashboard.getStatus()).isEqualTo(Status.DELETED);
        });
    }

    @DisplayName("팀 대시보드를 전체 조회합니다.")
    @Test
    void 팀_대시보드_전체_조회() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<TeamDashboard> teamDashboardPage = new PageImpl<>(
                List.of(teamDashboard),
                pageable,
                1);

        when(teamDashboardRepository.findForTeamDashboard(any(Member.class), any(Pageable.class)))
                .thenReturn(teamDashboardPage);

        // when
        TeamDashboardListResDto result = teamDashboardService.
                findForTeamDashboard(member.getEmail(), pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.teamDashboardInfoResDto()).hasSize(1);
    }

    @DisplayName("팀 대시보드를 상세 봅니다.")
    @Test
    void 팀_대시보드_상세보기() {
        // given
        Long dashboardId = 1L;
        when(teamDashboardRepository.findById(dashboardId)).thenReturn(Optional.of(teamDashboard));

        // when
        TeamDashboardInfoResDto result = teamDashboardService.findById(member.getEmail(), dashboardId);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("title");
            assertThat(result.description()).isEqualTo("description");
            assertThat(result.blockProgress()).isEqualTo(0.0);
        });
    }

    @DisplayName("삭제되었던 팀 대시보드를 복구합니다.")
    @Test
    void 팀_대시보드_복구() {
        // given
        Long dashboardId = 1L;
        when(teamDashboardRepository.findById(dashboardId)).thenReturn(Optional.of(deleteTeamDashboard));

        // when
        teamDashboardService.delete(member.getEmail(), dashboardId);

        // then
        assertAll(() -> {
            assertThat(deleteTeamDashboard.getStatus()).isEqualTo(Status.ACTIVE);
        });
    }

    @DisplayName("팀 대시보드에 참여합니다")
    @Test
    void 팀_대시보드_참여() {
        // given
        Long dashboardId = 1L;
        when(teamDashboardRepository.findById(dashboardId)).thenReturn(Optional.of(teamDashboard));

        // when
        teamDashboardService.joinTeam(member.getEmail(), dashboardId);
        TeamDashboardInfoResDto result = teamDashboardService.findById(member.getEmail(), dashboardId);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("title");
            assertThat(result.description()).isEqualTo("description");
            assertThat(result.blockProgress()).isEqualTo(0.0);
            assertThat(result.joinMembers().size()).isEqualTo(2);
        });
    }

    @DisplayName("이미 팀에 참여한 멤버가 다시 참여할 경우 예외가 발생합니다")
    @Test
    void 팀_대시보드_중복_참여_예외() {
        // given
        Long dashboardId = 1L;
        when(teamDashboardRepository.findById(dashboardId)).thenReturn(Optional.of(teamDashboard));

        teamDashboardService.joinTeam(member.getEmail(), dashboardId);

        // when & then
        assertThatThrownBy(() -> teamDashboardService.joinTeam(member.getEmail(), dashboardId))
                .isInstanceOf(AlreadyJoinedTeamException.class);
    }

    @DisplayName("팀 대시보드를 탈퇴합니다.")
    @Test
    void 팀_대시보드_탈퇴() {
        // given
        Long dashboardId = 1L;
        when(teamDashboardRepository.findById(dashboardId)).thenReturn(Optional.of(teamDashboard));

        // when
        teamDashboardService.joinTeam(member.getEmail(), dashboardId);
        teamDashboardService.leaveTeam(member.getEmail(), dashboardId);
        TeamDashboardInfoResDto result = teamDashboardService.findById(member.getEmail(), dashboardId);

        // then
        assertAll(() -> {
            assertThat(result.title()).isEqualTo("title");
            assertThat(result.description()).isEqualTo("description");
            assertThat(result.blockProgress()).isEqualTo(0.0);
            assertThat(result.joinMembers().size()).isEqualTo(1);
        });
    }

    @DisplayName("참여하지 않은 팀에서 탈퇴를 시도할 때 예외가 발생합니다.")
    @Test
    void 참여하지_않은_팀에서_탈퇴_시도() {
        // given
        Long dashboardId = 1L;
        when(teamDashboardRepository.findById(dashboardId)).thenReturn(Optional.of(teamDashboard));

        // when & then
        assertThatThrownBy(() -> teamDashboardService.leaveTeam(member.getEmail(), dashboardId))
                .isInstanceOf(NotMemberOfTeamException.class);
    }

    @DisplayName("팀원 초대 리스트를 조회합니다.")
    @Test
    void 팀_초대_멤버_조회() {
        // given
        String query = "email";
        when(teamDashboardRepository.findForMembersByQuery(query)).thenReturn(List.of(member));

        // when
        SearchMemberListResDto result = teamDashboardService.searchMembers(query);

        // then
        assertAll(() -> {
            assertThat(result.searchMembers().size()).isEqualTo(1);
        });
    }

}