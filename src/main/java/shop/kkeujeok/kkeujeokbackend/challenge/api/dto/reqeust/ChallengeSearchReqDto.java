package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "챌린지 검색 요청 DTO")
public record ChallengeSearchReqDto(
        @Schema(description = "검색 키워드", example = "조깅")
        String keyWord, // 검색 조건은 선택적일 수 있으므로 @NotNull 제외

        @Schema(description = "카테고리", example = "EXERCISE")
        String category // 검색 조건은 선택적일 수 있으므로 @NotNull 제외
) {
    public static ChallengeSearchReqDto from(String keyWord, String category) {
        return ChallengeSearchReqDto.builder()
                .keyWord(keyWord)
                .category(category)
                .build();
    }
}
