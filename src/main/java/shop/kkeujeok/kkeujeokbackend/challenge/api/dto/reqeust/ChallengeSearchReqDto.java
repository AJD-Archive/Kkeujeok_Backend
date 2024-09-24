package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust;

import lombok.Builder;

@Builder
public record ChallengeSearchReqDto(
        String keyWord,
        String category
) {
    public static ChallengeSearchReqDto from(String keyWord, String category) {
        return ChallengeSearchReqDto.builder()
                .keyWord(keyWord)
                .category(category)
                .build();
    }
}
