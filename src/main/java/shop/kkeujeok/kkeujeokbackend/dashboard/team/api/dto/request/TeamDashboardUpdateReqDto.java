package shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.request;

import java.util.List;

public record TeamDashboardUpdateReqDto(
        String title,
        String description,
        List<String> invitedEmails,
        List<String> invitedNicknamesAndTags
) {
}
