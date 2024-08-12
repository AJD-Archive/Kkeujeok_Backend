package shop.kkeujeok.kkeujeokbackend.dashboard.personal.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.dashboard.exception.DashboardNotFoundException;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.repository.PersonalDashboardRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.exception.DashboardAccessDeniedException;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
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

        return PersonalDashboardInfoResDto.of(member, dashboard);
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
                personalDashboardUpdateReqDto.isPublic(),
                personalDashboardUpdateReqDto.category());

        return PersonalDashboardInfoResDto.of(member, dashboard);
    }

    // 개인 대시보드 전체 조회
    public PersonalDashboardListResDto findForPersonalDashboard(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Page<PersonalDashboard> personalDashboards = personalDashboardRepository.
                findForPersonalDashboard(member, pageable);

        List<PersonalDashboardInfoResDto> personalDashboardInfoResDtoList = personalDashboards.stream()
                .map(p -> PersonalDashboardInfoResDto.of(member, p))
                .toList();

        return PersonalDashboardListResDto
                .from(personalDashboardInfoResDtoList, PageInfoResDto.from(personalDashboards));
    }

    // 개인 대시보드 상세조회
    public PersonalDashboardInfoResDto findById(String email, Long dashboardId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        PersonalDashboard dashboard = personalDashboardRepository.findById(dashboardId)
                .orElseThrow(DashboardNotFoundException::new);

        double blockProgress = personalDashboardRepository.calculateCompletionPercentage(dashboard.getId());

        return PersonalDashboardInfoResDto.detailOf(member, dashboard, blockProgress);
    }

    // 개인 대시보드 삭제 유무 업데이트 (논리 삭제)
    @Transactional
    public void delete(String email, Long dashboardId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        PersonalDashboard dashboard = personalDashboardRepository.findById(dashboardId)
                .orElseThrow(DashboardNotFoundException::new);

        verifyMemberIsAuthor(dashboard, member);

        dashboard.statusUpdate();
    }

    private void verifyMemberIsAuthor(PersonalDashboard dashboard, Member member) {
        if (!member.equals(dashboard.getMember())) {
            throw new DashboardAccessDeniedException();
        }
    }

}
