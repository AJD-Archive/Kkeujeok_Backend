package shop.kkeujeok.kkeujeokbackend.member.mypage.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.request.MyPageUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.MyPageInfoResDto;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class MyPageServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MyPageService myPageService;

    private MyPageUpdateReqDto myPageUpdateReqDto;
    private MyPageInfoResDto myPageInfoResDto;
    private Member member;

    @BeforeEach
    void setUp() {
        myPageUpdateReqDto = new MyPageUpdateReqDto("nickname", "introduction");
        myPageInfoResDto = new MyPageInfoResDto("picture", "email", "name", "nickname", SocialType.GOOGLE, "introduction");
        member = Member.builder()
                .email("email")
                .name("name")
                .nickname("nickname")
                .socialType(SocialType.GOOGLE)
                .introduction("introduction")
                .picture("picture")
                .build();
    }

    @DisplayName("프로필을 조회합니다.")
    @Test
    void 프로필을_조회합니다() {
        // Given
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));

        // When
        MyPageInfoResDto result = myPageService.findMyProfileByEmail("email");

        // Then
        assertEquals(myPageInfoResDto.email(), result.email());
        assertEquals(myPageInfoResDto.name(), result.name());
        assertEquals(myPageInfoResDto.nickName(), result.nickName());
        assertEquals(myPageInfoResDto.socialType(), result.socialType());
        assertEquals(myPageInfoResDto.introduction(), result.introduction());
        assertEquals(myPageInfoResDto.picture(), result.picture());
    }

    // 프로필 정보 수정
    @DisplayName("프로필 정보를 수정합니다.")
    @Test
    void 프로필_정보를_수정합니다() {
        // Given
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        MyPageUpdateReqDto newMyPageUpdateReqDto = new MyPageUpdateReqDto("newNickname", "newIntroduction");
        // When
        MyPageInfoResDto result = myPageService.update("email", newMyPageUpdateReqDto);

        // Then
        assertEquals("newNickname", result.nickName());
        assertEquals("newIntroduction", result.introduction());

        verify(memberRepository, times(1)).findByEmail("email");
    }

    // 팀 대시보드 정보 조회

    // 챌린지 정보 조회

    // 알림 조회
}
