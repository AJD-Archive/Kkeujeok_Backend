package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;

class FileTest {

    private Document document;
    private File file;

    @BeforeEach
    void setUp() {
        document = Document.builder()
                .title("documentTitle")
                .build();

        file = File.builder()
                .email("email")
                .title("fileTitle")
                .content("content")
                .document(document)
                .build();
    }

    @DisplayName("파일의 모든 값을 수정합니다.")
    @Test
    void 파일_수정() {
        // given
        String updateTitle = "Updated Title";
        String updateContent = "Updated Content";

        // when
        file.update(updateTitle, updateContent);

        // then
        assertAll(() -> {
            assertThat(file.getTitle()).isEqualTo(updateTitle);
            assertThat(file.getContent()).isEqualTo(updateContent);
        });
    }

    @DisplayName("파일의 논리 삭제 상태를 수정합니다.")
    @Test
    void 파일의_논리_삭제_상태를_수정합니다() {
        // given
        assertThat(file.getStatus()).isEqualTo(Status.ACTIVE);

        // when
        file.statusUpdate();

        // then
        assertThat(file.getStatus()).isEqualTo(Status.DELETED);

        // when
        file.statusUpdate();

        // then
        assertThat(file.getStatus()).isEqualTo(Status.ACTIVE);
    }
}
