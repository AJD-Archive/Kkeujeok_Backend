package shop.kkeujeok.kkeujeokbackend.dashboard.personal.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.dashboard.exception.DashboardNotFoundException;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.repository.PersonalDashboardRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.exception.DashboardAccessDeniedException;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.exception.MemberNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonalDashboardService {

    private final PersonalDashboardRepository personalDashboardRepository;
    private final MemberRepository memberRepository;

    // 개인 대시보드 저장
    @Transactional
    public PersonalDashboardInfoResDto save(String email, PersonalDashboardSaveReqDto personalDashboardSaveReqDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        PersonalDashboard dashboard = personalDashboardSaveReqDto.toEntity(member);

        personalDashboardRepository.save(dashboard);

        return PersonalDashboardInfoResDto.from(dashboard);
    }

    // 개인 대시보드 수정
    @Transactional
    public PersonalDashboardInfoResDto update(String email,
                                              Long dashboardId,
                                              PersonalDashboardUpdateReqDto personalDashboardUpdateReqDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        PersonalDashboard dashboard = personalDashboardRepository.findById(dashboardId)
                .orElseThrow(DashboardNotFoundException::new);

        verifyMemberIsAuthor(dashboard, member);

        dashboard.update(personalDashboardUpdateReqDto.title(),
                personalDashboardUpdateReqDto.description(),
                personalDashboardUpdateReqDto.category());

        return PersonalDashboardInfoResDto.from(dashboard);
    }

    private void verifyMemberIsAuthor(PersonalDashboard dashboard, Member member) {
        if (!member.equals(dashboard.getMember())) {
            throw new DashboardAccessDeniedException();
        }
    }

}
