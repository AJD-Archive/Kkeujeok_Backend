package shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

@Builder
@Schema(description = "팀 대시보드 목록 응답 DTO")
public record TeamDashboardListResDto(
        @Schema(description = "팀 대시보드 정보 리스트")
        @NotNull(message = "팀 대시보드 정보 리스트는 필수입니다.")
        List<TeamDashboardInfoResDto> teamDashboardInfoResDto,

        @Schema(description = "페이지 정보")
        @NotNull(message = "페이지 정보는 필수입니다.")
        PageInfoResDto pageInfoResDto
) {
    public static TeamDashboardListResDto of(List<TeamDashboardInfoResDto> teamDashboards,
                                             PageInfoResDto pageInfoResDto) {
        return TeamDashboardListResDto.builder()
                .teamDashboardInfoResDto(teamDashboards)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }

    public static TeamDashboardListResDto from(List<TeamDashboardInfoResDto> teamDashboards) {
        return TeamDashboardListResDto.builder()
                .teamDashboardInfoResDto(teamDashboards)
                .build();
    }
}
