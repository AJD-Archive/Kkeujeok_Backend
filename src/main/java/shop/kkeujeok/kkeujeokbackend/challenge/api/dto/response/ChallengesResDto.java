package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Cycle;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

@Schema(description = "챌린지 목록 응답 DTO")
public record ChallengesResDto(
        @Schema(description = "챌린지 요약 정보 리스트")
        @NotNull(message = "챌린지 요약 정보 리스트는 필수입니다.")
        List<ChallengeSummary> challengeSummaries,

        @Schema(description = "페이지 정보")
        @NotNull(message = "페이지 정보는 필수입니다.")
        PageInfoResDto pageInfoResDto
) {
    public static ChallengesResDto of(List<ChallengeSummary> challengeSummaries, PageInfoResDto pageInfo) {
        return new ChallengesResDto(challengeSummaries, pageInfo);
    }

    @Builder
    @Schema(description = "챌린지 요약 정보 DTO")
    public record ChallengeSummary(
            @Schema(description = "챌린지 ID", example = "1")
            @NotNull(message = "챌린지 ID는 필수입니다.")
            Long challengeId,

            @Schema(description = "대표 이미지 URL", example = "http://example.com/image.jpg")
            @NotNull(message = "대표 이미지 URL은 필수입니다.")
            String representImage,

            @Schema(description = "챌린지 제목", example = "매일 아침 조깅하기")
            @NotNull(message = "챌린지 제목은 필수입니다.")
            String title,

            @Schema(description = "주기", example = "WEEKLY")
            @NotNull(message = "주기는 필수입니다.")
            Cycle cycle,

            @Schema(description = "주기 상세정보", example = "[\"MON\", \"WED\", \"FRI\"]")
            @NotNull(message = "주기 상세정보는 필수입니다.")
            List<CycleDetail> cycleDetails,

            @Schema(description = "생성 일시", example = "2023-10-01T12:00:00")
            @NotNull(message = "생성 일시는 필수입니다.")
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
