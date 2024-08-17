package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.repository.TeamDashboardRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;
import shop.kkeujeok.kkeujeokbackend.global.config.JpaAuditingConfig;
import shop.kkeujeok.kkeujeokbackend.global.config.QuerydslConfig;

@DataJpaTest
@Import({JpaAuditingConfig.class, QuerydslConfig.class})
@ActiveProfiles("test")
class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private TeamDashboardRepository teamDashboardRepository;

    private TeamDashboard teamDashboard;
    private Document document1;
    private Document document2;
    private Document document3;

    @BeforeEach
    void setUp() {
        teamDashboard = TeamDashboard.builder()
                .title("title")
                .description("description")
                .build();

        document1 = Document.builder()
                .title("Document Title 1")
                .teamDashboard(teamDashboard)
                .build();

        document2 = Document.builder()
                .title("Document Title 2")
                .teamDashboard(teamDashboard)
                .build();

        document3 = Document.builder()
                .title("Document Title 3")
                .teamDashboard(teamDashboard)
                .build();

        teamDashboardRepository.save(teamDashboard);
        documentRepository.save(document1);
        documentRepository.save(document2);
        documentRepository.save(document3);
    }

    @DisplayName("팀 대시보드 ID로 문서를 전체 조회합니다.")
    @Test
    void 팀_대시보드_ID로_문서를_전체_조회합니다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Document> documents = documentRepository.findByDocumentWithTeamDashboard(teamDashboard.getId(), pageable);

        // then
        assertThat(documents.getContent().size()).isEqualTo(3);
        assertThat(documents.getContent()).extracting("teamDashboard.Id").containsOnly(teamDashboard.getId());
    }

    @DisplayName("문서를 논리 삭제 상태별로 전체 조회합니다.")
    @Test
    void 문서를_논리_삭제_상태별로_전체_조회합니다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        document1.statusUpdate();

        // when
        Page<Document> documents = documentRepository.findByDocumentWithTeamDashboard(teamDashboard.getId(), pageable);

        // then
        assertThat(documents.getContent().size()).isEqualTo(2);
    }
}
