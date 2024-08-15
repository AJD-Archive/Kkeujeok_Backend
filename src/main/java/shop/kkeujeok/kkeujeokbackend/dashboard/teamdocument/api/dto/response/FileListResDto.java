package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response;

import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

import java.util.List;

@Builder
public record FileListResDto(
        List<FileInfoResDto> fileInfoResDto,
        PageInfoResDto pageInfoResDto
) {
    public static FileListResDto from(List<FileInfoResDto> fileInfoResDto, PageInfoResDto pageInfoResDto) {
        return FileListResDto.builder()
                .fileInfoResDto(fileInfoResDto)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
