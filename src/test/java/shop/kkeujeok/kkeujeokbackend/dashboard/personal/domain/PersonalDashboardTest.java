package shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;

class PersonalDashboardTest {

    private PersonalDashboard personalDashboard;

    @BeforeEach
    void setUp() {
        personalDashboard = PersonalDashboard.builder()
                .title("title")
                .description("description")
                .isPublic(false)
                .category("category")
                .build();
    }

    @DisplayName("개인 대시보드의 모든 값을 수정합니다.")
    @Test
    void 개인_대시보드_수정() {
        // given
        String updateTitle = "updateTitle";
        String updateDescription = "updateDescription";
        boolean updateIsPublic = true;
        String updateCategory = "category";

        // when
        personalDashboard.update(updateTitle, updateDescription, updateIsPublic, updateCategory);

        // then
        assertAll(() -> {
            assertThat(personalDashboard.getTitle()).isEqualTo(updateTitle);
            assertThat(personalDashboard.getDescription()).isEqualTo(updateDescription);
            assertThat(personalDashboard.isPublic()).isEqualTo(updateIsPublic);
            assertThat(personalDashboard.getCategory()).isEqualTo(updateCategory);
        });
    }

    @DisplayName("개인 대시보드 논리 삭제 상태를 수정합니다.")
    @Test
    void 개인_대시보드_상태_수정() {
        // given
        assertThat(personalDashboard.getStatus()).isEqualTo(Status.ACTIVE);

        // when
        personalDashboard.statusUpdate();

        // then
        assertThat(personalDashboard.getStatus()).isEqualTo(Status.DELETED);

        // when
        personalDashboard.statusUpdate();

        // then
        assertThat(personalDashboard.getStatus()).isEqualTo(Status.ACTIVE);
    }

}