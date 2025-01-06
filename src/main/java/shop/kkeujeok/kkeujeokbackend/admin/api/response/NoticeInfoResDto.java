package shop.kkeujeok.kkeujeokbackend.admin.api.response;

import lombok.Builder;

@Builder
public record NoticeInfoResDto(
        Long id,
        String version,
        String title,
        String content,
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