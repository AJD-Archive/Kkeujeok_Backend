package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.File;

@Builder
public record FileInfoResDto(
        Long fileId,
        String email,
        String title,
        String content
) {
    public static FileInfoResDto from(File file) {
        return FileInfoResDto.builder()
                .fileId(file.getId())
                .email(file.getEmail())
                .title(file.getTitle())
                .content(file.getContent())
                .build();
    }
}
