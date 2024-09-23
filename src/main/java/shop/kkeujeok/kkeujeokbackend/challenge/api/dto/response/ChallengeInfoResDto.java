package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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
        String authorProfileImage,
        String blockName,
        int participantCount,
        boolean isParticipant,
        boolean isAuthor,
        Set<ChallengeCompletedMemberInfoResDto> completedMembers
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
                .blockName(challenge.getBlockName())
                .participantCount(challenge.getParticipantsCount())
                .isAuthor(true)
                .isParticipant(false)
                .completedMembers(Collections.emptySet())
                .build();
    }

    public static ChallengeInfoResDto of(Challenge challenge, boolean isParticipant, boolean isAuthor,
                                         Set<ChallengeCompletedMemberInfoResDto> completedMembers) {
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
                .blockName(challenge.getBlockName())
                .participantCount(challenge.getParticipantsCount())
                .isParticipant(isParticipant)
                .isAuthor(isAuthor)
                .completedMembers(completedMembers)
                .build();
    }
}
