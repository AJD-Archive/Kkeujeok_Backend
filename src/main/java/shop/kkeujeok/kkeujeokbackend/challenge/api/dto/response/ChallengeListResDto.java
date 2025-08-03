package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response;

import java.util.List;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

public record ChallengeListResDto(
        List<ChallengeInfoResDto> challenges,
        PageInfoResDto pageInfo
) {
    public static ChallengeListResDto of(List<ChallengeInfoResDto> challenges,
                                         PageInfoResDto pageInfo) {
        return new ChallengeListResDto(challenges, pageInfo);
    }
}
