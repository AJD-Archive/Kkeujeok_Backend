package shop.kkeujeok.kkeujeokbackend.block.api.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Builder
public record BlockInfoResDto(
        String title,
        String contents,
        Progress progress,
        String deadLine,
        String nickname,
        int dDay
) {
    public static BlockInfoResDto of(Block block, Member member) {
        return BlockInfoResDto.builder()
                .title(block.getTitle())
                .contents(block.getContents())
                .progress(block.getProgress())
                .deadLine(block.getDeadLine())
                .nickname(member.getNickname())
                .dDay(calculateDDay(block.getDeadLine()))
                .build();
    }

    private static int calculateDDay(String deadlineStr) {
        LocalDateTime deadline = LocalDateTime.parse(deadlineStr, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        LocalDate today = LocalDate.now();
        LocalDate deadlineDate = deadline.toLocalDate();
        return Math.toIntExact(ChronoUnit.DAYS.between(today, deadlineDate));
    }

}
