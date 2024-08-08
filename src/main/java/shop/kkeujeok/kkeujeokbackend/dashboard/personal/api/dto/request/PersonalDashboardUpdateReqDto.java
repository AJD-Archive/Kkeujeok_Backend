package shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request;

public record PersonalDashboardUpdateReqDto(
        String title,
        String description,
        boolean isPublic,
        String category
) {
}
