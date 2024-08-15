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
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.File;
import shop.kkeujeok.kkeujeokbackend.global.config.JpaAuditingConfig;
import shop.kkeujeok.kkeujeokbackend.global.config.QuerydslConfig;

@DataJpaTest
@Import({JpaAuditingConfig.class, QuerydslConfig.class})
@ActiveProfiles("test")
class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private DocumentRepository documentRepository;

    private Document document;
    private File file1;
    private File file2;
    private File file3;

    @BeforeEach
    void setUp() {
        document = Document.builder()
                .title("Document Title")
                .build();

        file1 = File.builder()
                .email("email1")
                .title("title1")
                .content("content1")
                .document(document)
                .build();

        file2 = File.builder()
                .email("email2")
                .title("title2")
                .content("content2")
                .document(document)
                .build();

        file3 = File.builder()
                .email("email3")
                .title("title3")
                .content("content3")
                .document(document)
                .build();

        documentRepository.save(document);
        fileRepository.save(file1);
        fileRepository.save(file2);
        fileRepository.save(file3);
    }

    @DisplayName("Document ID로 파일을 전체 조회합니다.")
    @Test
    void Document_ID로_파일을_전체_조회합니다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<File> files = fileRepository.findByFilesWithDocumentId(document.getId(), pageable);

        // then
        assertThat(files.getContent().size()).isEqualTo(3);
        assertThat(files.getContent()).extracting("document.id").containsOnly(document.getId());
    }

    @DisplayName("파일을 논리 삭제 상태별로 전체 조회합니다.")
    @Test
    void 파일을_논리_삭제_상태별로_전체_조회합니다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        file1.statusUpdate();

        // when
        Page<File> files = fileRepository.findByFilesWithDocumentId(document.getId(), pageable);

        // then
        assertThat(files.getContent().size()).isEqualTo(2);
    }
}
