package shop.kkeujeok.kkeujeokbackend.block.api.dto.request;

import java.util.List;

public record BlockSequenceUpdateReqDto(
        List<Long> notStartedList,
        List<Long> inProgressList,
        List<Long> completedList
) {
}
