package shop.kkeujeok.kkeujeokbackend.dashboard.personal.application;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.dashboard.exception.DashboardDeletedException;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.ChallengeMemberMapping;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.repository.challengeMemberMapping.ChallengeMemberMappingRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.exception.DashboardNotFoundException;
import shop.kkeujeok.kkeujeokbackend.dashboard.exception.UnauthorizedAccessException;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.request.PersonalDashboardUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardCategoriesResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.api.dto.response.PersonalDashboardPageListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.repository.PersonalDashboardRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.exception.DashboardAccessDeniedException;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.exception.MemberNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonalDashboardService {

    private final PersonalDashboardRepository personalDashboardRepository;
    private final MemberRepository memberRepository;
    private final ChallengeMemberMappingRepository challengeMemberMappingRepository;

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
    public PersonalDashboardListResDto findForPersonalDashboard(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        List<PersonalDashboard> personalDashboards = personalDashboardRepository.findForPersonalDashboard(member);

        List<PersonalDashboardInfoResDto> personalDashboardInfoResDtoList = personalDashboards.stream()
                .map(p -> PersonalDashboardInfoResDto.of(member, p))
                .toList();

        return PersonalDashboardListResDto.of(personalDashboardInfoResDtoList);
    }

    // 개인 대시보드 상세조회
    public PersonalDashboardInfoResDto findById(String email, Long dashboardId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        PersonalDashboard dashboard = personalDashboardRepository.findById(dashboardId)
                .orElseThrow(DashboardNotFoundException::new);

        checkIfDashboardIsDeleted(dashboard);
        validateDashboardAccess(dashboard, member);

        double blockProgress = personalDashboardRepository.calculateCompletionPercentage(dashboard.getId());

        return PersonalDashboardInfoResDto.detailOf(member, dashboard, blockProgress);
    }

    private void checkIfDashboardIsDeleted(PersonalDashboard dashboard) {
        if (dashboard.isDeleted()) {
            throw new DashboardDeletedException();
        }
    }

    private void validateDashboardAccess(PersonalDashboard dashboard, Member member) {
        if (!dashboard.getMember().equals(member)) {
            throw new UnauthorizedAccessException();
        }
    }

    // 개인 대시보드 카테고리 조회
    public PersonalDashboardCategoriesResDto findCategoriesForDashboard(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        Set<String> categories = personalDashboardRepository.findCategoriesForDashboard(member);

        return PersonalDashboardCategoriesResDto.from(categories);
    }

    // 개인 대시보드 삭제 유무 업데이트 (논리 삭제)
    @Transactional
    public void delete(String email, Long dashboardId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        PersonalDashboard dashboard = personalDashboardRepository.findById(dashboardId)
                .orElseThrow(DashboardNotFoundException::new);

        verifyMemberIsAuthor(dashboard, member);

        List<ChallengeMemberMapping> challengeMemberMappings = dashboard.getChallengeMemberMappings();
        dashboard.getChallengeMemberMappings().removeAll(challengeMemberMappings);

        dashboard.statusUpdate();
    }


    private void verifyMemberIsAuthor(PersonalDashboard dashboard, Member member) {
        if (!member.equals(dashboard.getMember())) {
            throw new DashboardAccessDeniedException();
        }
    }

    // isPublic이 true인 대시보드 조회 메서드
    public PersonalDashboardPageListResDto findPublicPersonalDashboards(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        Page<PersonalDashboard> publicDashboards = personalDashboardRepository.findPublicPersonalDashboard(member,
                pageable);

        List<PersonalDashboardInfoResDto> publicDashboardInfoResDtoList = publicDashboards.stream()
                .map(dashboard -> PersonalDashboardInfoResDto.of(member, dashboard))
                .toList();

        return PersonalDashboardPageListResDto.of(publicDashboardInfoResDtoList, PageInfoResDto.from(publicDashboards));
    }

    // 개인 대시보드 전체 조회(페이지네이션 적용)
    public PersonalDashboardPageListResDto findForPersonalDashboard(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        // Repository를 통해 페이징된 개인 대시보드 조회
        Page<PersonalDashboard> personalDashboards = personalDashboardRepository.findForPersonalDashboard(member,
                pageable);

        // DTO 변환
        List<PersonalDashboardInfoResDto> personalDashboardInfoResDtoList = personalDashboards.stream()
                .map(p -> PersonalDashboardInfoResDto.of(member, p))
                .toList();

        // 페이징 정보를 포함한 응답 반환
        return PersonalDashboardPageListResDto.of(personalDashboardInfoResDtoList,
                PageInfoResDto.from(personalDashboards));
    }
}