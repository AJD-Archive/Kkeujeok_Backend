package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record ChallengeSearchReqDto(
        @NotEmpty
        String keyWord
) {
    public static ChallengeSearchReqDto from(String keyWord) {
        return ChallengeSearchReqDto.builder()
                .keyWord(keyWord)
                .build();
    }
}
