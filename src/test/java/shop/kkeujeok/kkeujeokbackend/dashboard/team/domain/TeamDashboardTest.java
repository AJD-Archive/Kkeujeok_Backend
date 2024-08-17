package shop.kkeujeok.kkeujeokbackend.dashboard.team.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;

class TeamDashboardTest {

    private Member member;
    private TeamDashboard teamDashboard;

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
        teamDashboard = TeamDashboard.builder()
                .title("title")
                .description("description")
                .build();
    }

    @DisplayName("팀 대시보드의 모든 값을 수정합니다.")
    @Test
    void 팀_대시보드_수정() {
        // given
        String updateTitle = "updateTitle";
        String updateDescription = "updateDescription";

        // when
        teamDashboard.update(updateTitle, updateDescription);

        // then
        assertAll(() -> {
            assertThat(teamDashboard.getTitle()).isEqualTo(updateTitle);
            assertThat(teamDashboard.getDescription()).isEqualTo(updateDescription);
        });
    }

    @DisplayName("팀 대시보드 논리 삭제 상태를 수정합니다.")
    @Test
    void 팀_대시보드_상태_수정() {
        // given
        assertThat(teamDashboard.getStatus()).isEqualTo(Status.ACTIVE);

        // when
        teamDashboard.statusUpdate();

        // then
        assertThat(teamDashboard.getStatus()).isEqualTo(Status.DELETED);

        // when
        teamDashboard.statusUpdate();

        // then
        assertThat(teamDashboard.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @DisplayName("팀 대시보드에 참가합니다.")
    @Test
    void 팀_대시보드_참가() {
        // given
        assertTrue(teamDashboard.getTeamDashboardMemberMappings().isEmpty());

        // when
        teamDashboard.addMember(member);

        // then
        assertThat(teamDashboard.getTeamDashboardMemberMappings().size()).isEqualTo(1);
    }

    @DisplayName("팀 대시보드 탈퇴")
    @Test
    void 팀_대시보드_탈퇴() {
        // given
        teamDashboard.addMember(member);
        assertThat(teamDashboard.getTeamDashboardMemberMappings().size()).isEqualTo(1);

        // when
        teamDashboard.removeMember(member);

        // then
        assertThat(teamDashboard.getTeamDashboardMemberMappings().size()).isEqualTo(0);
    }

}