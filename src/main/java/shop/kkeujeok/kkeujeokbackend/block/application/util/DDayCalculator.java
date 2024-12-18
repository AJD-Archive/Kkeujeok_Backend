package shop.kkeujeok.kkeujeokbackend.block.application.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DDayCalculator {
    
    public static String calculate(String deadlineStr) {
        LocalDate deadlineDate = LocalDateTime.parse(deadlineStr, DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))
                .toLocalDate();
        LocalDate today = LocalDate.now();

        long daysBetween = ChronoUnit.DAYS.between(today, deadlineDate);

        if (today.isBefore(deadlineDate)) {
            return "D-" + daysBetween;
        }
        if (today.isEqual(deadlineDate)) {
            return "D-Day";
        }

        return "D+" + Math.abs(daysBetween);
    }

}
