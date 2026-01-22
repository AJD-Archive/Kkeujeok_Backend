package shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Builder
@Schema(description = "팀원 검색 결과 목록 응답 DTO")
public record SearchMemberListResDto(
        @Schema(description = "검색된 회원 정보 리스트")
        List<SearchMemberInfoResDto> searchMembers
) {
    public static SearchMemberListResDto from(List<Member> members) {
        return SearchMemberListResDto.builder()
                .searchMembers(members.stream()
                        .map(SearchMemberInfoResDto::from)
                        .toList())
                .build();
    }

    @Builder
    @Schema(description = "검색된 회원 정보 DTO")
    private record SearchMemberInfoResDto(
            @Schema(description = "회원 ID", example = "1")
            Long id,

            @Schema(description = "프로필 사진 URL", example = "http://example.com/profile.jpg")
            String picture,

            @Schema(description = "이메일", example = "user@example.com")
            String email
    ) {
        private static SearchMemberInfoResDto from(Member member) {
            return SearchMemberInfoResDto.builder()
                    .id(member.getId())
                    .picture(member.getPicture())
                    .email(member.getEmail())
                    .build();
        }
    }

}
