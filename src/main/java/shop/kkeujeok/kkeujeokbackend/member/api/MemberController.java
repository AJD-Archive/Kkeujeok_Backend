package shop.kkeujeok.kkeujeokbackend.member.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.MyProfileInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.application.MyPageService;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MyPageService myPageService;

    public MemberController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @GetMapping("/profile")
    public RspTemplate<MyProfileInfoResDto> myProfileInfo(@CurrentUserEmail String email) {
        MyProfileInfoResDto memberResDto = myPageService.findMyProfileByEmail(email);
        return new RspTemplate<>(HttpStatus.OK, "내 프로필 정보", memberResDto);
    }
}
