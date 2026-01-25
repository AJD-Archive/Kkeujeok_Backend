package shop.kkeujeok.kkeujeokbackend.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
@Schema(description = "페이지 정보 응답 DTO")
public record PageInfoResDto(
        @Schema(description = "현재 페이지 번호", example = "0")
        int currentPage,

        @Schema(description = "전체 페이지 수", example = "10")
        int totalPages,

        @Schema(description = "전체 아이템 수", example = "100")
        long totalItems
) {
    public static <T> PageInfoResDto from(Page<T> entityPage) {
        return PageInfoResDto.builder()
                .currentPage(entityPage.getNumber())
                .totalPages(entityPage.getTotalPages())
                .totalItems(entityPage.getTotalElements())
                .build();
    }
}
