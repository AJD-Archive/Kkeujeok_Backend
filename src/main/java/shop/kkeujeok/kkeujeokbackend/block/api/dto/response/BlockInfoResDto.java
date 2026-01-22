package shop.kkeujeok.kkeujeokbackend.block.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.Type;

@Builder
@Schema(description = "블록 정보 응답 DTO")
public record BlockInfoResDto(
        @Schema(description = "블록 ID", example = "1")
        Long blockId,

        @Schema(description = "블록 제목", example = "프로젝트 기획")
        String title,

        @Schema(description = "블록 내용", example = "프로젝트 기획서 작성 및 검토")
        String contents,

        @Schema(description = "진행 상태", example = "NOT_STARTED")
        Progress progress,

        @Schema(description = "블록 타입", example = "BASIC")
        Type type,

        @Schema(description = "대시보드 타입", example = "PERSONAL")
        String dType,

        @Schema(description = "시작 날짜", example = "2023-10-01")
        String startDate,

        @Schema(description = "마감 기한", example = "2023-10-10")
        String deadLine,

        @Schema(description = "작성자 닉네임", example = "홍길동")
        String nickname,

        @Schema(description = "작성자 프로필 사진 URL", example = "http://example.com/profile.jpg")
        String picture,

        @Schema(description = "D-Day", example = "D-5")
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
