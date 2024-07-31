package shop.kkeujeok.kkeujeokbackend.member.mypage.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.auth.exception.EmailNotFoundException;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.request.MyPageUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.MyPageInfoResDto;

@Service
@Transactional(readOnly = true)
public class MyPageService {
    private final MemberRepository memberRepository;

    public MyPageService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 프로필 정보 조회
    public MyPageInfoResDto findMyProfileByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();

        return MyPageInfoResDto.From(member);
    }

    // 프로필 정보 수정
    @Transactional
    public MyPageInfoResDto update(String email, MyPageUpdateReqDto myPageUpdateReqDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(EmailNotFoundException::new);

        member.update(myPageUpdateReqDto.nickname(), myPageUpdateReqDto.introduction());

        return MyPageInfoResDto.From(member);
    }

    // 팀 대시보드 정보 조회

    // 챌린지 정보 조회

    // 알림 조회
}
