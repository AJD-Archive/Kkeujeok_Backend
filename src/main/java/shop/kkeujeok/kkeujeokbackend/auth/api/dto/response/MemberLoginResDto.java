package shop.kkeujeok.kkeujeokbackend.auth.api.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@Builder
@Schema(description = "회원 로그인 응답 DTO")
public record MemberLoginResDto(
        @Schema(description = "회원 정보")
        Member findMember
) {
    public static MemberLoginResDto from(Member member) {
        return MemberLoginResDto.builder()
                .findMember(member)
                .build();
    }
}
