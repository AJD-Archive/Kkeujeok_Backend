package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response;

import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

@Builder
public record PersonalDashboardListResDto(
        List<PersonalDashboardInfoResDto> personalDashboardListResDto,
        PageInfoResDto pageInfoResDto
) {
    public static PersonalDashboardListResDto from(List<PersonalDashboardInfoResDto> personalDashboards,
                                                   PageInfoResDto pageInfoResDto) {
        return PersonalDashboardListResDto.builder()
                .personalDashboardListResDto(personalDashboards)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
