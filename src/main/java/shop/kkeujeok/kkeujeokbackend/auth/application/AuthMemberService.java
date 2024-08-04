package shop.kkeujeok.kkeujeokbackend.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.response.MemberLoginResDto;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.response.UserInfo;
import shop.kkeujeok.kkeujeokbackend.auth.exception.EmailNotFoundException;
import shop.kkeujeok.kkeujeokbackend.auth.exception.ExistsMemberEmailException;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.Role;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.nickname.application.NicknameService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthMemberService {

    private final MemberRepository memberRepository;
    private final NicknameService nicknameService;

    @Transactional
    public MemberLoginResDto saveUserInfo(UserInfo userInfo, SocialType provider) {
        validateNotFoundEmail(userInfo.email());

        Member member = getExistingMemberOrCreateNew(userInfo, provider);

        validateSocialType(member, provider);

        return MemberLoginResDto.from(member);
    }

    private void validateNotFoundEmail(String email) {
        if (email == null) {
            throw new EmailNotFoundException();
        }
    }

    private Member getExistingMemberOrCreateNew(UserInfo userInfo, SocialType provider) {
        return memberRepository.findByEmail(userInfo.email()).orElseGet(() -> createMember(userInfo, provider));
    }

    private Member createMember(UserInfo userInfo, SocialType provider) {
        String userPicture = getUserPicture(userInfo.picture());
        String name = unionName(userInfo.name(), userInfo.nickname());
        String nickname = nicknameService.getRandomNickname(); // 랜덤 닉네임 생성

        return memberRepository.save(
                Member.builder()
                        .status(Status.ACTIVE)
                        .email(userInfo.email())
                        .name(name)
                        .picture(userPicture)
                        .socialType(provider)
                        .role(Role.ROLE_USER)
                        .firstLogin(true)
                        .nickname(nickname)
                        .introduction("자기 소개를 입력해 주세요.")
                        .build()
        );
    }

    private String unionName(String name, String nickname) {
        return nickname != null ? nickname : name;
    }
    
    private String getUserPicture(String picture) {
        return Optional.ofNullable(picture)
                .map(this::convertToHighRes)
                .orElseThrow();
    }

    private String convertToHighRes(String url){
        return url.replace("s96-c", "s2048-c");
    }

    private void validateSocialType(Member member, SocialType provider) {
        if (!provider.equals(member.getSocialType())) {
            throw new ExistsMemberEmailException();
        }
    }
}
