package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Builder
public record ChallengeInfoResDto(
        String title,
        String contents,
        List<CycleDetail> cycleDetails,
        LocalDate startDate,
        LocalDate endDate,
        String representImage,
        String authorName,
        String authorProfileImage
) {
    public static ChallengeInfoResDto of(Challenge challenge, Member member) {
        return ChallengeInfoResDto.builder()
                .title(challenge.getTitle())
                .contents(challenge.getContents())
                .cycleDetails(challenge.getCycleDetails())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .representImage(challenge.getRepresentImage())
                .authorName(member.getNickname())
                .authorProfileImage(member.getPicture())
                .build();
    }
}
