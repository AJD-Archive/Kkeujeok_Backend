package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "개인 대시보드 목록 응답 DTO")
public record PersonalDashboardListResDto(
        @Schema(description = "개인 대시보드 정보 리스트")
        @NotNull(message = "개인 대시보드 정보 리스트는 필수입니다.")
        List<PersonalDashboardInfoResDto> personalDashboardListResDto
) {
    public static PersonalDashboardListResDto of(List<PersonalDashboardInfoResDto> personalDashboards) {
        return PersonalDashboardListResDto.builder()
                .personalDashboardListResDto(personalDashboards)
                .build();
    }
}
