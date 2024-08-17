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
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;
import shop.kkeujeok.kkeujeokbackend.global.config.JpaAuditingConfig;
import shop.kkeujeok.kkeujeokbackend.global.config.QuerydslConfig;

@DataJpaTest
@Import({JpaAuditingConfig.class, QuerydslConfig.class})
@ActiveProfiles("test")
class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;

    private Document document1;
    private Document document2;
    private Document document3;

    @BeforeEach
    void setUp() {
        document1 = Document.builder()
                .title("Document Title 1")
                .build();

        document2 = Document.builder()
                .title("Document Title 2")
                .build();

        document3 = Document.builder()
                .title("Document Title 3")
                .build();

        documentRepository.save(document1);
        documentRepository.save(document2);
        documentRepository.save(document3);
    }

    @DisplayName("팀 대시보드 ID로 문서를 전체 조회합니다.")
    @Test
    void 팀_대시보드_ID로_문서를_전체_조회합니다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Long teamDashboardId = 1L; // 가정: document 엔티티에 teamDashboardId 필드가 있다면

        // when
        Page<Document> documents = documentRepository.findByDocumentWithTeamDashboard(teamDashboardId, pageable);

        // then
        assertThat(documents.getContent().size()).isEqualTo(3);
        assertThat(documents.getContent()).extracting("teamDashboardId").containsOnly(teamDashboardId);
    }

    @DisplayName("문서를 논리 삭제 상태별로 전체 조회합니다.")
    @Test
    void 문서를_논리_삭제_상태별로_전체_조회합니다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        document1.statusUpdate(); // 문서를 논리 삭제 상태로 업데이트

        // when
        Page<Document> documents = documentRepository.findByDocumentWithTeamDashboard(1L, pageable); // 가정: document 엔티티에 teamDashboardId 필드가 있다면

        // then
        assertThat(documents.getContent().size()).isEqualTo(2); // 논리 삭제 상태가 아닌 문서만 조회
    }
}
