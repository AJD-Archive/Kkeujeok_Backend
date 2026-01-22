package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Category;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Cycle;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Schema(description = "챌린지 생성 요청 DTO")
public record ChallengeSaveReqDto(
        @Schema(description = "챌린지 제목", example = "매일 아침 조깅하기")
        @NotNull(message = "제목은 필수 입력값입니다.")
        String title,

        @Schema(description = "챌린지 내용", example = "매일 아침 30분씩 조깅을 합니다.")
        @NotNull(message = "내용은 필수 입력값입니다.")
        String contents,

        @Schema(description = "카테고리", example = "EXERCISE")
        @NotNull(message = "카테고리는 필수 입력값입니다.")
        Category category,

        @Schema(description = "주기", example = "WEEKLY")
        @NotNull(message = "주기는 필수 입력값입니다.")
        Cycle cycle,

        @Schema(description = "주기 상세정보", example = "[\"MON\", \"WED\", \"FRI\"]")
        @NotNull(message = "주기 상세정보는 필수 입력값입니다.")
        List<CycleDetail> cycleDetails,

        @Schema(description = "종료 날짜", example = "2023-12-31")
        @NotNull(message = "종료 날짜는 필수 입력값입니다.")
        LocalDate endDate,

        @Schema(description = "블록 이름", example = "조깅")
        @NotNull(message = "블록 이름은 필수 입력값입니다.")
        String blockName
) {
    public Challenge toEntity(Member member, String representImage) {
        return Challenge.builder()
                .status(Status.ACTIVE)
                .title(title)
                .contents(contents)
                .category(category)
                .cycle(cycle)
                .cycleDetails(cycleDetails)
                .startDate(LocalDate.now())
                .endDate(endDate)
                .representImage(representImage)
                .member(member)
                .blockName(blockName)
                .build();
    }
}
