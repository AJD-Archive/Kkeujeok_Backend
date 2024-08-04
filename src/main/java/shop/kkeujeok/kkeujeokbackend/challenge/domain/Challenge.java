package shop.kkeujeok.kkeujeokbackend.challenge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.challenge.converter.CycleDetailsConverter;
import shop.kkeujeok.kkeujeokbackend.challenge.exception.InvalidCycleException;
import shop.kkeujeok.kkeujeokbackend.global.entity.BaseEntity;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @Convert(converter = CycleDetailsConverter.class)
    @Column(name = "cycle_details")
    private List<CycleDetail> cycleDetails;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "represent_image")
    private String representImage;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Challenge(Status status,
                      String title,
                      String contents,
                      List<CycleDetail> cycleDetails,
                      LocalDate startDate,
                      LocalDate endDate,
                      String representImage,
                      Member member) {
        this.status = status;
        this.title = title;
        this.contents = contents;
        this.cycleDetails = cycleDetails;
        this.startDate = startDate;
        this.endDate = endDate;
        this.representImage = representImage;
        this.member = member;
    }

    public void validateCycleDetails(Cycle cycle) {
        Set<CycleDetail> seenDetails = new HashSet<>();
        for (CycleDetail cycleDetail : cycleDetails) {
            validateCycleDetailUniqueness(seenDetails, cycleDetail);
            validateCycleDetailMatch(cycleDetail, cycle);
        }
    }

    private void validateCycleDetailUniqueness(Set<CycleDetail> seenDetails, CycleDetail cycleDetail) {
        if (!seenDetails.add(cycleDetail)) {
            throw new InvalidCycleException("중복된 주기 세부 항목이 발견되었습니다: "
                    + cycleDetail.getDescription() + cycleDetail.getCycle().name());
        }
    }

    private void validateCycleDetailMatch(CycleDetail cycleDetail, Cycle cycle) {
        if (cycleDetail.getCycle() != cycle) {
            throw new InvalidCycleException("주기와 일치하지 않는 세부 항목이 있습니다: "
                    + cycleDetail.getDescription() + cycleDetail.getCycle().name());
        }
    }
}
