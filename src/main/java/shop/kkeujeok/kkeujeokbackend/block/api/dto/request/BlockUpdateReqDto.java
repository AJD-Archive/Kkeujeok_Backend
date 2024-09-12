package shop.kkeujeok.kkeujeokbackend.block.api.dto.request;

public record BlockUpdateReqDto(
        String title,
        String contents,
        String startDate,
        String deadLine
) {
}
