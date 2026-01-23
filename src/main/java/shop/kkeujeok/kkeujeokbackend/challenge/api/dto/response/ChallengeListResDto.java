package shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

@Schema(description = "챌린지 목록 응답 DTO")
public record ChallengeListResDto(
        @Schema(description = "챌린지 정보 리스트")
        @NotNull(message = "챌린지 정보 리스트는 필수입니다.")
        List<ChallengeInfoResDto> challengeInfoResDto,

        @Schema(description = "페이지 정보")
        @NotNull(message = "페이지 정보는 필수입니다.")
        PageInfoResDto pageInfoResDto
) {
    public static ChallengeListResDto of(List<ChallengeInfoResDto> challengeInfoResDto,
                                         PageInfoResDto pageInfo) {
        return new ChallengeListResDto(challengeInfoResDto, pageInfo);
    }
}
