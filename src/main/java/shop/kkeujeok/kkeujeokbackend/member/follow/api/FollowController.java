package shop.kkeujeok.kkeujeokbackend.member.follow.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.request.FollowAcceptReqDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.request.FollowReqDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.FollowAcceptResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.FollowResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.application.FollowService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/member/follow")
public class FollowController {

    private final FollowService followService;

    @PostMapping("")
    public RspTemplate<FollowResDto> save(@CurrentUserEmail String email,
                                          @RequestBody FollowReqDto followReqDto) {
        return new RspTemplate<>(HttpStatus.OK,
                "친구 추가 요청",
                followService.save(email, followReqDto));
    }

    @PostMapping("/accept")
    public RspTemplate<FollowAcceptResDto> accept(@RequestBody FollowAcceptReqDto followAcceptReqDto) {
        return new RspTemplate<>(HttpStatus.OK,
                "친구 추가 수락",
                followService.accept(followAcceptReqDto));
    }
}
