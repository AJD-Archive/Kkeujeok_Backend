package shop.kkeujeok.kkeujeokbackend.amdin.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.amdin.api.response.NoticeInfoResDto;
import shop.kkeujeok.kkeujeokbackend.amdin.api.response.NoticeListResDto;
import shop.kkeujeok.kkeujeokbackend.amdin.domain.Notice;
import shop.kkeujeok.kkeujeokbackend.amdin.domain.repository.NoticeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeListResDto findAll() {
        List<Notice> notices = noticeRepository.findAll();

        List<NoticeInfoResDto> noticeInfoResDtos = notices.stream()
                .map(notice -> NoticeInfoResDto.from(
                        notice.getId(),
                        notice.getVersion(),
                        notice.getTitle(),
                        notice.getContents(),
                        String.valueOf(notice.getCreatedAt()))
                ).toList();

        return NoticeListResDto.from(noticeInfoResDtos);
    }

}
