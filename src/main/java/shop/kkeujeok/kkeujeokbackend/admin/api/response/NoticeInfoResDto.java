package shop.kkeujeok.kkeujeokbackend.admin.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "공지사항 정보 응답 DTO")
public record NoticeInfoResDto(
        @Schema(description = "공지사항 ID", example = "1")
        Long id,

        @Schema(description = "버전", example = "1.0.0")
        String version,

        @Schema(description = "제목", example = "서비스 점검 안내")
        String title,

        @Schema(description = "내용", example = "서비스 점검이 예정되어 있습니다.")
        String content,

        @Schema(description = "생성 일시", example = "2023-10-01T12:00:00")
        String createdAt
) {
    public static NoticeInfoResDto from(Long id, String version, String title, String content, String createdAt) {
        return NoticeInfoResDto.builder()
                .id(id)
                .version(version)
                .title(title)
                .content(content)
                .createdAt(createdAt)
                .build();
    }
}