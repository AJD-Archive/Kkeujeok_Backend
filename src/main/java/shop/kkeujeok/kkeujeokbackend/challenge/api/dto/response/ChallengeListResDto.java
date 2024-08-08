package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response;

import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

@Builder
public record ChallengeListResDto(
        List<ChallengeInfoResDto> challengeInfoResDto,
        PageInfoResDto pageInfoResDto
) {
    public static ChallengeListResDto of(List<ChallengeInfoResDto> challengeInfoResDtoList,
                                         PageInfoResDto pageInfoResDto) {
        return ChallengeListResDto.builder()
                .challengeInfoResDto(challengeInfoResDtoList)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
