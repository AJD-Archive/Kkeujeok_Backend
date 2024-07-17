package shop.kkeujeok.kkeujeokbackend.auth.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.response.MemberLoginResDto;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.response.UserInfo;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.Role;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthMemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private AuthMemberService authMemberService;

    private UserInfo userInfo;
    private SocialType provider;
    private Member member;

    @BeforeEach
    void setUp() {
        userInfo = new UserInfo("이메일", "이름", "사진", "닉네임");
        provider = SocialType.GOOGLE;
        member = Member.builder()
                .status(Status.A)
                .email(userInfo.email())
                .name(userInfo.name())
                .picture(userInfo.picture())
                .socialType(provider)
                .role(Role.ROLE_USER)
                .firstLogin(true)
                .nickname(userInfo.nickname())
                .build();
    }

    @DisplayName("신규 회원을 저장합니다.")
    @Test
    void 신규_회원을_저장합니다() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberLoginResDto result = authMemberService.saveUserInfo(userInfo, provider);

        assertThat(result).isNotNull();
        verify(memberRepository).findByEmail(userInfo.email());
        verify(memberRepository).save(any(Member.class));
    }

    @DisplayName("회원 정보가 올바르게 저장되는지 확인합니다.")
    @Test
    void 회원_정보가_올바르게_저장되는지_확인합니다() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MemberLoginResDto result = authMemberService.saveUserInfo(userInfo, provider);

        assertThat(result).isNotNull();
        assertThat(result.findMember().getEmail()).isEqualTo(userInfo.email());
        assertThat(result.findMember().getName()).isEqualTo(userInfo.name());
        verify(memberRepository).findByEmail(userInfo.email());
        verify(memberRepository).save(any(Member.class));
    }

    @DisplayName("소셜 타입이 일치하지 않는 경우 예외를 던집니다.")
    @Test
    void 소셜_타입이_일치하지_않는_경우_예외를_던집니다() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        SocialType invalidProvider = SocialType.KAKAO;

        assertThrows(RuntimeException.class, () -> authMemberService.saveUserInfo(userInfo, invalidProvider));

        verify(memberRepository).findByEmail(userInfo.email());
    }

    @DisplayName("이메일이 null인 경우 예외를 던집니다.")
    @Test
    void 이메일이_null인_경우_예외를_던집니다() {
        UserInfo invalidUserInfo = new UserInfo(null, "이름", "사진", "닉네임");

        assertThrows(RuntimeException.class, () -> authMemberService.saveUserInfo(invalidUserInfo, provider));
    }

    @DisplayName("이름이 null인 경우 예외를 던집니다.")
    @Test
    void 이름이_null인_경우_예외를_던집니다() {
        UserInfo userInfoWithNullName = new UserInfo("이메일", null, "사진", "닉네임");

        assertThrows(RuntimeException.class, () -> authMemberService.saveUserInfo(userInfoWithNullName, provider));
    }

    @DisplayName("사진이 null인 경우 예외를 던집니다.")
    @Test
    void 사진이_null인_경우_예외를_던집니다() {
        UserInfo userInfoWithNullPicture = new UserInfo("이메일", "이름", null, "닉네임");

        assertThrows(RuntimeException.class, () -> authMemberService.saveUserInfo(userInfoWithNullPicture, provider));
    }

    @DisplayName("닉네임이 null인 경우 예외를 던집니다.")
    @Test
    void 닉네임이_null인_경우_예외를_던집니다() {
        UserInfo userInfoWithNullNickname = new UserInfo("이메일", "이름", "사진", null);

        assertThrows(RuntimeException.class, () -> authMemberService.saveUserInfo(userInfoWithNullNickname, provider));
    }
}
