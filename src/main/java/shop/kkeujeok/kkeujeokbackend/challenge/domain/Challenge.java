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

    @Enumerated(value = EnumType.STRING)
    private Category category;

    private Cycle cycle;

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

    private String blockName;

    @Builder
    private Challenge(Status status,
                      String title,
                      String contents,
                      Category category,
                      Cycle cycle,
                      List<CycleDetail> cycleDetails,
                      LocalDate startDate,
                      LocalDate endDate,
                      String representImage,
                      Member member,
                      String blockName) {
        validateCycleDetails(cycle, cycleDetails);

        this.status = status;
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.cycle = cycle;
        this.cycleDetails = cycleDetails;
        this.startDate = startDate;
        this.endDate = endDate;
        this.representImage = representImage;
        this.member = member;
        this.blockName = blockName;
    }

    public void update(String updateTitle, String updateContents, List<CycleDetail> updateCycleDetails,
                       LocalDate updateEndDate, String updateRepresentImage, String updateBlockName) {
        validateCycleDetails(cycle, cycleDetails);

        if (hasChanges(updateTitle, updateContents, updateCycleDetails, updateEndDate,
                updateRepresentImage, updateBlockName)) {
            this.title = updateTitle;
            this.contents = updateContents;
            this.cycleDetails = updateCycleDetails;
            this.endDate = updateEndDate;
            this.representImage = updateRepresentImage;
            this.blockName = updateBlockName;
        }
    }

    private void validateCycleDetails(Cycle cycle, List<CycleDetail> cycleDetails) {
        Set<CycleDetail> distinctCycleDetails = new HashSet<>();

        cycleDetails.forEach(cycleDetail -> {
            validateCycleDetailUniqueness(distinctCycleDetails, cycleDetail);
            validateCycleDetailMatch(cycle, cycleDetail);
        });
    }

    private void validateCycleDetailUniqueness(Set<CycleDetail> seenDetails, CycleDetail cycleDetail) {
        if (!seenDetails.add(cycleDetail)) {
            throw InvalidCycleException.forDuplicateDetail();
        }
    }

    private void validateCycleDetailMatch(Cycle cycle, CycleDetail cycleDetail) {
        if (cycleDetail.getCycle() != cycle) {
            throw InvalidCycleException.forMismatchDetail();
        }
    }

    private boolean hasChanges(String updateTitle, String updateContents, List<CycleDetail> updateCycleDetails,
                               LocalDate updateEndDate, String updateRepresentImage, String blockName) {
        return !this.title.equals(updateTitle) ||
                !this.contents.equals(updateContents) ||
                !this.cycleDetails.equals(updateCycleDetails) ||
                !this.endDate.equals(updateEndDate) ||
                !this.representImage.equals(updateRepresentImage) ||
                !this.blockName.equals(blockName);
    }

    public void updateStatus() {
        this.status = (this.status == Status.ACTIVE) ? Status.DELETED : Status.ACTIVE;
    }
}
