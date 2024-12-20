package shop.kkeujeok.kkeujeokbackend.member.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.request.MyPageUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.MyPageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.PersonalDashboardsAndChallengesResDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.TeamDashboardsAndChallengesResDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.application.MyPageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MyPageService myPageService;

    @GetMapping("/mypage")
    public RspTemplate<MyPageInfoResDto> myProfileInfo(@CurrentUserEmail String email) {
        MyPageInfoResDto memberResDto = myPageService.findMyProfileByEmail(email);
        return new RspTemplate<>(HttpStatus.OK, "내 프로필 정보", memberResDto);
    }

    @PatchMapping("/mypage")
    public RspTemplate<MyPageInfoResDto> update(@CurrentUserEmail String email,
                                                @RequestBody MyPageUpdateReqDto myPageUpdateReqDto) {
        return new RspTemplate<>(HttpStatus.OK, "내 프로필 정보 수정", myPageService.update(email, myPageUpdateReqDto));
    }

    @GetMapping("/mypage/dashboard-challenges")
    public RspTemplate<TeamDashboardsAndChallengesResDto> getTeamDashboardsAndChallenges(@CurrentUserEmail String email,
                                                                                         @RequestParam(name = "requestEmail") String requestEmail,
                                                                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                                                                         @RequestParam(name = "size", defaultValue = "10") int size) {
        TeamDashboardsAndChallengesResDto response = myPageService.findTeamDashboardsAndChallenges(email, requestEmail,
                PageRequest.of(page, size));
        return new RspTemplate<>(HttpStatus.OK, "대시보드와 챌린지 정보 조회", response);
    }

    @GetMapping("/mypage/{friendId}")
    public RspTemplate<MyPageInfoResDto> getFriendProfileInfo(@PathVariable Long friendId) {
        MyPageInfoResDto friendProfile = myPageService.findFriendProfile(friendId);
        return new RspTemplate<>(HttpStatus.OK, "친구 프로필 정보 조회", friendProfile);
    }

    @GetMapping("/mypage/{friendId}/dashboard-challenges")
    public RspTemplate<PersonalDashboardsAndChallengesResDto> getPersonalDashboardsAndChallenges(@PathVariable Long friendId,
                                                                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                                                                 @RequestParam(name = "size", defaultValue = "10") int size) {
        PersonalDashboardsAndChallengesResDto response = myPageService.findFriendDashboardsAndChallenges(friendId,
                PageRequest.of(page, size));
        return new RspTemplate<>(HttpStatus.OK, "대시보드와 챌린지 정보 조회", response);
    }
}
