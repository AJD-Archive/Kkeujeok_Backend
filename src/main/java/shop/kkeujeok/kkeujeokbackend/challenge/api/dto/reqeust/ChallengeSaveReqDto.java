package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
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
        @NotNull(message = "주기는 필수 입력값입니다.")
        Cycle cycle,
        @NotNull(message = "주기 상세정보는 필수 입력값입니다.")
        List<CycleDetail> cycleDetails,
        @NotBlank(message = "시작 날짜는 필수 입력값입니다.")
        LocalDate startDate,
        @NotBlank(message = "종료 날짜는 필수 입력값입니다.")
        LocalDate endDate,
        String representImage
) {
    public Challenge toEntity(Member member) {
        return Challenge.builder()
                .status(Status.A)
                .title(title)
                .contents(contents)
                .cycleDetails(cycleDetails)
                .startDate(startDate)
                .endDate(endDate)
                .representImage(representImage)
                .member(member)
                .build();
    }
}
