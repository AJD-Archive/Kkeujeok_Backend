package shop.kkeujeok.kkeujeokbackend.block.api.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.Type;

@Builder
public record BlockInfoResDto(
        Long blockId,
        String title,
        String contents,
        Progress progress,
        Type type,
        String dType,
        String startDate,
        String deadLine,
        String nickname,
        String picture,
        int dDay
) {
    public static BlockInfoResDto from(Block block) {
        return BlockInfoResDto.builder()
                .blockId(block.getId())
                .title(block.getTitle())
                .contents(block.getContents())
                .progress(block.getProgress())
                .type(block.getType())
                .dType(block.getDashboard().getDType())
                .startDate(block.getStartDate())
                .deadLine(block.getDeadLine())
                .nickname(block.getMember().getNickname())
                .picture(block.getMember().getPicture())
                .dDay(calculateDDay(block.getStartDate(), block.getDeadLine()))
                .build();
    }

    private static int calculateDDay(String startDateStr, String deadlineStr) {
        LocalDateTime start = LocalDateTime.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        LocalDateTime deadline = LocalDateTime.parse(deadlineStr, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));

        LocalDate startDate = start.toLocalDate();
        LocalDate deadlineDate = deadline.toLocalDate();

        return Math.toIntExact(ChronoUnit.DAYS.between(startDate, deadlineDate));
    }

}
