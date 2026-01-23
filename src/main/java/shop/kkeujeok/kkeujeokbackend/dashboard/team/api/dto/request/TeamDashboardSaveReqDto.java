package shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Schema(description = "팀 대시보드 생성 요청 DTO")
public record TeamDashboardSaveReqDto(
        @Schema(description = "대시보드 제목", example = "팀 프로젝트")
        @NotBlank(message = "필수 입력값 입니다.")
        String title,

        @Schema(description = "대시보드 설명", example = "팀 프로젝트를 위한 대시보드입니다.")
        @NotBlank(message = "필수 입력값 입니다.")
        @Size(max = 300)
        String description,

        @Schema(description = "초대할 이메일 리스트", example = "[\"user1@example.com\", \"user2@example.com\"]")
        @NotNull(message = "초대할 이메일 리스트는 필수입니다.")
        List<String> invitedEmails,

        @Schema(description = "초대할 닉네임 및 태그 리스트", example = "[\"user1#1234\", \"user2#5678\"]")
        @NotNull(message = "초대할 닉네임 및 태그 리스트는 필수입니다.")
        List<String> invitedNicknamesAndTags
) {
    public TeamDashboard toEntity(Member member) {
        return TeamDashboard.builder()
                .title(title)
                .description(description)
                .member(member)
                .build();
    }
}
