package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Cycle;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;
import shop.kkeujeok.kkeujeokbackend.challenge.exception.InvalidCycleException;
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

        @NotNull(message = "시작 날짜는 필수 입력값입니다.")
        LocalDate startDate,

        @NotNull(message = "종료 날짜는 필수 입력값입니다.")
        LocalDate endDate,

        String representImage
) {
    public Challenge toEntity(Member member) {
        validateCycleDetails();
        return Challenge.builder()
                .status(Status.ACTIVE)
                .title(title)
                .contents(contents)
                .cycle(cycle)
                .cycleDetails(cycleDetails)
                .startDate(startDate)
                .endDate(endDate)
                .representImage(representImage)
                .member(member)
                .build();
    }

    private void validateCycleDetails() {
        Set<CycleDetail> distinctCycleDetails = new HashSet<>();

        cycleDetails.forEach(cycleDetail -> {
            validateCycleDetailUniqueness(distinctCycleDetails, cycleDetail);
            validateCycleDetailMatch(cycleDetail);
        });
    }

    private void validateCycleDetailUniqueness(Set<CycleDetail> seenDetails, CycleDetail cycleDetail) {
        if (!seenDetails.add(cycleDetail)) {
            throw InvalidCycleException.forDuplicateDetail();
        }
    }

    private void validateCycleDetailMatch(CycleDetail cycleDetail) {
        if (cycleDetail.getCycle() != cycle) {
            throw InvalidCycleException.forMismatchDetail();
        }
    }
}
