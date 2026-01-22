package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "챌린지 검색 요청 DTO")
public record ChallengeSearchReqDto(
        @Schema(description = "검색 키워드", example = "조깅")
        String keyWord,

        @Schema(description = "카테고리", example = "EXERCISE")
        String category
) {
    public static ChallengeSearchReqDto from(String keyWord, String category) {
        return ChallengeSearchReqDto.builder()
                .keyWord(keyWord)
                .category(category)
                .build();
    }
}
