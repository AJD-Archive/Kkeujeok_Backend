package shop.kkeujeok.kkeujeokbackend.block.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.Type;

@Builder
@Schema(description = "블록 정보 응답 DTO")
public record BlockInfoResDto(
        @Schema(description = "블록 ID", example = "1")
        @NotNull(message = "블록 ID는 필수입니다.")
        Long blockId,

        @Schema(description = "블록 제목", example = "프로젝트 기획")
        @NotNull(message = "블록 제목은 필수입니다.")
        String title,

        @Schema(description = "블록 내용", example = "프로젝트 기획서 작성 및 검토")
        @NotNull(message = "블록 내용은 필수입니다.")
        String contents,

        @Schema(description = "진행 상태", example = "NOT_STARTED")
        @NotNull(message = "진행 상태는 필수입니다.")
        Progress progress,

        @Schema(description = "블록 타입", example = "BASIC")
        @NotNull(message = "블록 타입은 필수입니다.")
        Type type,

        @Schema(description = "대시보드 타입", example = "PERSONAL")
        @NotNull(message = "대시보드 타입은 필수입니다.")
        String dType,

        @Schema(description = "시작 날짜", example = "2023-10-01")
        @NotNull(message = "시작 날짜는 필수입니다.")
        String startDate,

        @Schema(description = "마감 기한", example = "2023-10-10")
        @NotNull(message = "마감 기한은 필수입니다.")
        String deadLine,

        @Schema(description = "작성자 닉네임", example = "홍길동")
        @NotNull(message = "작성자 닉네임은 필수입니다.")
        String nickname,

        @Schema(description = "작성자 프로필 사진 URL", example = "http://example.com/profile.jpg")
        @NotNull(message = "작성자 프로필 사진은 필수입니다.")
        String picture,

        @Schema(description = "D-Day", example = "D-5")
        @NotNull(message = "D-Day는 필수입니다.")
        String dDay
) {
    public static BlockInfoResDto from(Block block, String dDay) {
        return new BlockInfoResDto(
                block.getId(),
                block.getTitle(),
                block.getContents(),
                block.getProgress(),
                block.getType(),
                block.getDashboard().getDType(),
                block.getStartDate(),
                block.getDeadLine(),
                block.getMember().getNickname(),
                block.getMember().getPicture(),
                dDay
        );
    }

}
