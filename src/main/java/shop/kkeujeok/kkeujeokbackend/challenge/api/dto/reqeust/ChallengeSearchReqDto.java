package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust;

import lombok.Builder;

@Builder
public record ChallengeSearchReqDto(
        String keyWord
) {
    public static ChallengeSearchReqDto from(String keyWord) {
        return ChallengeSearchReqDto.builder()
                .keyWord(keyWord)
                .build();
    }
}
