package shop.kkeujeok.kkeujeokbackend.block.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;

class BlockTest {
    private String title;
    private String contents;
    private String startDate;
    private String deadLine;

    private Block block;

    @BeforeEach
    void setUp() {
        title = "title";
        contents = "contents";
        startDate = "2024.07.03 22:34";
        deadLine = "2024.07.27 22:34";

        block = Block.builder()
                .title(title)
                .contents(contents)
                .progress(Progress.NOT_STARTED)
                .startDate(startDate)
                .deadLine(deadLine)
                .build();
    }

    @DisplayName("블록의 모든 값을 수정합니다.")
    @Test
    void 블록_수정() {
        // given
        String updateTitle = "updateTitle";
        String updateContents = "updateContents";
        String updateStartDate = "updateStartDate";
        String updateDeadLine = "updateDeadLine";

        // when
        block.update(updateTitle, updateContents, updateStartDate, updateDeadLine);

        // then
        assertAll(() -> {
            assertThat(block.getTitle()).isEqualTo(updateTitle);
            assertThat(block.getContents()).isEqualTo(updateContents);
            assertThat(block.getDeadLine()).isEqualTo(updateDeadLine);
        });
    }

    @DisplayName("블록의 제목만 수정합니다.")
    @Test
    void 블록_제목_수정() {
        // given
        String updateTitle = "updateTitle";

        // when
        block.update(updateTitle, contents, startDate, deadLine);

        // then
        assertAll(() -> {
            assertThat(block.getTitle()).isEqualTo(updateTitle);
            assertThat(block.getContents()).isEqualTo(contents);
            assertThat(block.getDeadLine()).isEqualTo(deadLine);
        });
    }

    @DisplayName("블록의 내용만 수정합니다.")
    @Test
    void 블록_내용_수정() {
        // given
        String updateContents = "updateContents";

        // when
        block.update(title, updateContents, startDate, deadLine);

        // then
        assertAll(() -> {
            assertThat(block.getTitle()).isEqualTo(title);
            assertThat(block.getContents()).isEqualTo(updateContents);
            assertThat(block.getDeadLine()).isEqualTo(deadLine);
        });
    }

    @DisplayName("블록의 마감 기한만 수정합니다.")
    @Test
    void 블록_마감_기한_수정() {
        // given
        String updateDeadLine = "2024.07.28 22:34";

        // when
        block.update(title, contents, startDate, updateDeadLine);

        // then
        assertAll(() -> {
            assertThat(block.getTitle()).isEqualTo(title);
            assertThat(block.getContents()).isEqualTo(contents);
            assertThat(block.getDeadLine()).isEqualTo(updateDeadLine);
        });
    }

    @DisplayName("블록의 진행 상태를 수정합니다.")
    @Test
    void 블록_진행_상태_수정() {
        // given
        Progress progress = Progress.IN_PROGRESS;

        // when
        block.progressUpdate(progress);

        // then
        assertThat(block.getProgress()).isEqualTo(progress);
    }

    @DisplayName("블록의 논리 삭제 상태를 수정합니다.")
    @Test
    void 블록_논리_삭제_수정() {
        // given
        assertThat(block.getStatus()).isEqualTo(Status.ACTIVE);

        // when
        block.statusUpdate();

        // then
        assertThat(block.getStatus()).isEqualTo(Status.DELETED);

        // when
        block.statusUpdate();

        // then
        assertThat(block.getStatus()).isEqualTo(Status.ACTIVE);
    }

}