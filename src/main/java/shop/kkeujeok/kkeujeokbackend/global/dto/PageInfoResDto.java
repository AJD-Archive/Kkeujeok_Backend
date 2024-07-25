package shop.kkeujeok.kkeujeokbackend.global.dto;

import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record PageInfoResDto(
        int currentPage,
        int totalPages,
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
