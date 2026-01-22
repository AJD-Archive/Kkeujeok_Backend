package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

import java.util.List;

@Builder
@Schema(description = "개인 대시보드 페이징 목록 응답 DTO")
public record PersonalDashboardPageListResDto(
        @Schema(description = "개인 대시보드 정보 리스트")
        List<PersonalDashboardInfoResDto> personalDashboardInfoResDto,

        @Schema(description = "페이지 정보")
        PageInfoResDto pageInfoResDto
) {
    public static PersonalDashboardPageListResDto of(List<PersonalDashboardInfoResDto> personalDashboards,
                                                     PageInfoResDto pageInfoResDto) {
        return PersonalDashboardPageListResDto.builder()
                .personalDashboardInfoResDto(personalDashboards)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }

    public static PersonalDashboardPageListResDto from(List<PersonalDashboardInfoResDto> personalDashboards) {
        return PersonalDashboardPageListResDto.builder()
                .personalDashboardInfoResDto(personalDashboards)
                .build();
    }
}
