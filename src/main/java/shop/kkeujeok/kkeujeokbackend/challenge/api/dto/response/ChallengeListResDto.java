package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response;

import java.util.List;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

public record ChallengeListResDto(
        List<ChallengeInfoResDto> challengeInfoResDto,
        PageInfoResDto pageInfoResDto
) {
    public static ChallengeListResDto of(List<ChallengeInfoResDto> challengeInfoResDto,
                                         PageInfoResDto pageInfo) {
        return new ChallengeListResDto(challengeInfoResDto, pageInfo);
    }
}
