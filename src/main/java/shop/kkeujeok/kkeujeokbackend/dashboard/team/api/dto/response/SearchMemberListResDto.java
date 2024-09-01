package shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response;

import java.util.List;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Builder
public record SearchMemberListResDto(
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
    private record SearchMemberInfoResDto(
            Long id,
            String picture,
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
