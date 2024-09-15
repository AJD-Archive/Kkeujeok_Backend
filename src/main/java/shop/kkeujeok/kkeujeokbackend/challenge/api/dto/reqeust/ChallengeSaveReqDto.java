package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Category;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Cycle;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

public record ChallengeSaveReqDto(
        @NotNull(message = "제목은 필수 입력값입니다.")
        String title,

        @NotNull(message = "내용은 필수 입력값입니다.")
        String contents,

        @NotNull(message = "카테고리는 필수 입력값입니다.")
        Category category,

        @NotNull(message = "주기는 필수 입력값입니다.")
        Cycle cycle,

        @NotNull(message = "주기 상세정보는 필수 입력값입니다.")
        List<CycleDetail> cycleDetails,

        @NotNull(message = "종료 날짜는 필수 입력값입니다.")
        LocalDate endDate,

        String representImage,
        @NotNull(message = "블록 이름은 필수 입력값입니다.")

        String blockName
) {
    public Challenge toEntity(Member member) {
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
