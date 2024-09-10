package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Category;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Cycle;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;

@Builder
public record ChallengeInfoResDto(
        Long challengeId,
        String title,
        String contents,
        Category category,
        Cycle cycle,
        List<CycleDetail> cycleDetails,
        LocalDate startDate,
        LocalDate endDate,
        String representImage,
        String authorName,
        String authorProfileImage
) {
    public static ChallengeInfoResDto from(Challenge challenge) {
        return ChallengeInfoResDto.builder()
                .challengeId(challenge.getId())
                .title(challenge.getTitle())
                .contents(challenge.getContents())
                .category(challenge.getCategory())
                .cycle(challenge.getCycle())
                .cycleDetails(challenge.getCycleDetails())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .representImage(challenge.getRepresentImage())
                .authorName(challenge.getMember().getNickname())
                .authorProfileImage(challenge.getMember().getPicture())
                .build();
    }
}
