package shop.kkeujeok.kkeujeokbackend.member.mypage.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeListResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.application.ChallengeService;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.application.PersonalDashboardService;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.api.dto.response.TeamDashboardListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.application.TeamDashboardService;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.request.MyPageUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.MyPageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.TeamDashboardsAndChallengesResDto;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyPageServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PersonalDashboardService personalDashboardService;

    @Mock
    private TeamDashboardService teamDashboardService;

    @Mock
    private ChallengeService challengeService;

    @InjectMocks
    private MyPageService myPageService;

    private MyPageUpdateReqDto myPageUpdateReqDto;
    private MyPageInfoResDto myPageInfoResDto;
    private Member member;
    private Pageable pageable;
    private TeamDashboardListResDto teamDashboardListResDto;
    private ChallengeListResDto challengeListResDto;

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

        pageable = PageRequest.of(0, 10);

        teamDashboardListResDto = TeamDashboardListResDto.of(
                Collections.emptyList(),
                null
        );

        challengeListResDto = ChallengeListResDto.of(
                Collections.emptyList(),
                null
        );
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

    @DisplayName("팀 대시보드와 챌린지 정보를 조회합니다.")
    @Test
    void 팀_대시보드와_챌린지_정보를_조회합니다() {
        // Given
        String email = "test@example.com";

        when(teamDashboardService.findForTeamDashboard(email, pageable)).thenReturn(teamDashboardListResDto);
        when(challengeService.findChallengeForMemberId(email, pageable)).thenReturn(challengeListResDto);

        // When
        TeamDashboardsAndChallengesResDto result = myPageService.findTeamDashboardsAndChallenges(email,"test@example.com", pageable);

        // Then
        assertEquals(teamDashboardListResDto, result.teamDashboardList());
        assertEquals(challengeListResDto, result.challengeList());
    }
}
