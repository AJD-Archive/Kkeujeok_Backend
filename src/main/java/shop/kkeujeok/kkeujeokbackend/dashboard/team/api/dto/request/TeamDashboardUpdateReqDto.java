package shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "팀 대시보드 수정 요청 DTO")
public record TeamDashboardUpdateReqDto(
        @Schema(description = "대시보드 제목", example = "수정된 팀 프로젝트")
        @NotNull(message = "대시보드 제목은 필수입니다.")
        String title,

        @Schema(description = "대시보드 설명", example = "수정된 팀 프로젝트 설명입니다.")
        @NotNull(message = "대시보드 설명은 필수입니다.")
        String description,

        @Schema(description = "초대할 이메일 리스트", example = "[\"user3@example.com\"]")
        @NotNull(message = "초대할 이메일 리스트는 필수입니다.")
        List<String> invitedEmails,

        @Schema(description = "초대할 닉네임 및 태그 리스트", example = "[\"user3#9012\"]")
        @NotNull(message = "초대할 닉네임 및 태그 리스트는 필수입니다.")
        List<String> invitedNicknamesAndTags
) {
}
