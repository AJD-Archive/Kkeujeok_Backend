package shop.kkeujeok.kkeujeokbackend.admin.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "공지사항 목록 응답 DTO")
public record NoticeListResDto(
        @Schema(description = "공지사항 정보 리스트")
        @NotNull(message = "공지사항 정보 리스트는 필수입니다.")
        List<NoticeInfoResDto> noticeListResDto
) {
    public static NoticeListResDto from(List<NoticeInfoResDto> notices) {
        return NoticeListResDto.builder()
                .noticeListResDto(notices)
                .build();
    }
}
