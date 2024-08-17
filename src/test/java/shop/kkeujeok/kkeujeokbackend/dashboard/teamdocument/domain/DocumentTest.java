package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;

class DocumentTest {

    private TeamDashboard teamDashboard;
    private Document document;

    @BeforeEach
    void setUp() {
        teamDashboard = TeamDashboard.builder()
                .title("teamDashboardTitle")
                .description("teamDashboardDescription")
                .build();

        document = Document.builder()
                .title("documentTitle")
                .teamDashboard(teamDashboard)
                .build();
    }

    @DisplayName("문서의 모든 값을 수정합니다.")
    @Test
    void 문서의_모든_값을_수정합니다() {
        // given
        String updateTitle = "Updated Document Title";

        // when
        document.update(updateTitle);

        // then
        assertThat(document.getTitle()).isEqualTo(updateTitle);
    }

    @DisplayName("문서의 논리 삭제 상태를 수정합니다.")
    @Test
    void 문서의_논리_삭제_상태를_수정합니다() {
        // given
        assertThat(document.getStatus()).isEqualTo(Status.ACTIVE);

        // when
        document.statusUpdate();

        // then
        assertThat(document.getStatus()).isEqualTo(Status.DELETED);

        // when
        document.statusUpdate();

        // then
        assertThat(document.getStatus()).isEqualTo(Status.ACTIVE);
    }
}
