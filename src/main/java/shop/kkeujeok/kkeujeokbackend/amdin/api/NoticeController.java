package shop.kkeujeok.kkeujeokbackend.amdin.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.kkeujeok.kkeujeokbackend.amdin.api.response.NoticeListResDto;
import shop.kkeujeok.kkeujeokbackend.amdin.application.NoticeService;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("")
    public RspTemplate<NoticeListResDto> findAll() {
        return new RspTemplate<>(HttpStatus.OK, "공지 조회", noticeService.findAll());
    }

}
