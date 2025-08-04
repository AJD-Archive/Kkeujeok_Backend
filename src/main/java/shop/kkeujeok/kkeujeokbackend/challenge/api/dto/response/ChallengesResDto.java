package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Cycle;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

public record ChallengesResDto(
        List<ChallengeSummary> challenges,
        PageInfoResDto pageInfoResDto
) {
    public static ChallengesResDto of(List<ChallengeSummary> challenges, PageInfoResDto pageInfo) {
        return new ChallengesResDto(challenges, pageInfo);
    }

    @Builder
    public record ChallengeSummary(
            Long challengeId,
            String representImage,
            String title,
            Cycle cycle,
            List<CycleDetail> cycleDetails,
            LocalDateTime createdAt
    ) {
        public static ChallengeSummary from(Challenge challenge) {
            return ChallengeSummary.builder()
                    .challengeId(challenge.getId())
                    .representImage(challenge.getRepresentImage())
                    .title(challenge.getTitle())
                    .cycle(challenge.getCycle())
                    .cycleDetails(challenge.getCycleDetails())
                    .createdAt(challenge.getCreatedAt())
                    .build();
        }
    }
}


