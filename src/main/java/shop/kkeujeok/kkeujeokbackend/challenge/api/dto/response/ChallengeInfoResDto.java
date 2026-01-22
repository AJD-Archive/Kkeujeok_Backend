package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Category;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Cycle;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;

@Builder
@Schema(description = "챌린지 상세 정보 응답 DTO")
public record ChallengeInfoResDto(
        @Schema(description = "챌린지 ID", example = "1")
        Long challengeId,

        @Schema(description = "작성자 ID", example = "1")
        Long authorId,

        @Schema(description = "챌린지 제목", example = "매일 아침 조깅하기")
        String title,

        @Schema(description = "챌린지 내용", example = "매일 아침 30분씩 조깅을 합니다.")
        String contents,

        @Schema(description = "카테고리", example = "EXERCISE")
        Category category,

        @Schema(description = "주기", example = "WEEKLY")
        Cycle cycle,

        @Schema(description = "주기 상세정보", example = "[\"MON\", \"WED\", \"FRI\"]")
        List<CycleDetail> cycleDetails,

        @Schema(description = "시작 날짜", example = "2023-10-01")
        LocalDate startDate,

        @Schema(description = "종료 날짜", example = "2023-12-31")
        LocalDate endDate,

        @Schema(description = "대표 이미지 URL", example = "http://example.com/image.jpg")
        String representImage,

        @Schema(description = "작성자 이름", example = "홍길동")
        String authorName,

        @Schema(description = "작성자 프로필 이미지 URL", example = "http://example.com/profile.jpg")
        String authorProfileImage,

        @Schema(description = "블록 이름", example = "조깅")
        String blockName,

        @Schema(description = "참여자 수", example = "10")
        int participantCount,

        @Schema(description = "참여 여부", example = "true")
        boolean isParticipant,

        @Schema(description = "작성자 여부", example = "false")
        boolean isAuthor,

        @Schema(description = "완료한 멤버 정보")
        Set<ChallengeCompletedMemberInfoResDto> completedMembers,

        @Schema(description = "생성 일시", example = "2023-10-01T12:00:00")
        LocalDateTime createdAt
) {
    public static ChallengeInfoResDto from(Challenge challenge) {
        return ChallengeInfoResDto.builder()
                .challengeId(challenge.getId())
                .authorId(challenge.getMember().getId())
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
                .createdAt(challenge.getCreatedAt())
                .build();
    }

    public static ChallengeInfoResDto of(Challenge challenge, boolean isParticipant, boolean isAuthor,
                                         Set<ChallengeCompletedMemberInfoResDto> completedMembers) {
        return ChallengeInfoResDto.builder()
                .challengeId(challenge.getId())
                .authorId(challenge.getMember().getId())
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
                .createdAt(challenge.getCreatedAt())
                .build();
    }
}
