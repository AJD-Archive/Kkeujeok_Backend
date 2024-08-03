package shop.kkeujeok.kkeujeokbackend.member.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.request.MyPageUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.MyPageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.application.MyPageService;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MyPageService myPageService;

    public MemberController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @GetMapping("/mypage")
    public RspTemplate<MyPageInfoResDto> myProfileInfo(@CurrentUserEmail String email) {
        MyPageInfoResDto memberResDto = myPageService.findMyProfileByEmail(email);
        return new RspTemplate<>(HttpStatus.OK, "내 프로필 정보", memberResDto);
    }

    @PatchMapping("/mypage")
    public RspTemplate<MyPageInfoResDto> update(@CurrentUserEmail String email,
                                                @RequestBody MyPageUpdateReqDto myPageUpdateReqDto) {
        return new RspTemplate<>(HttpStatus.OK, "내 프로필 정보 수정", myPageService.update(email,myPageUpdateReqDto));
    }
}
