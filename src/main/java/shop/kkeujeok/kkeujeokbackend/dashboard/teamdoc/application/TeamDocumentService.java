package shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.dashboard.exception.DashboardNotFoundException;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.repository.TeamDashboardRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request.FindTeamDocumentReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request.TeamDocumentReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.request.TeamDocumentUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response.FindTeamDocumentResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response.TeamDocumentCategoriesResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response.TeamDocumentDetailResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.api.dto.response.TeamDocumentResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.domain.TeamDocument;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.domain.repository.TeamDocumentRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdoc.exception.TeamDocumentNotFoundException;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.exception.MemberNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamDocumentService {

    private final TeamDocumentRepository teamDocumentRepository;
    private final TeamDashboardRepository teamDashboardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TeamDocumentResDto save(String email, TeamDocumentReqDto documentReqDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
        TeamDashboard teamDashboard = teamDashboardRepository.findById(documentReqDto.teamDashboardId())
                .orElseThrow(DashboardNotFoundException::new);

        TeamDocument document = documentReqDto.toEntity(member, teamDashboard);

        teamDocumentRepository.save(document);

        return TeamDocumentResDto.from(document);
    }

    public TeamDocumentDetailResDto findById(Long teamDocumentId) {
        TeamDocument teamDocument = teamDocumentRepository.findById(teamDocumentId)
                .orElseThrow(TeamDocumentNotFoundException::new);

        return TeamDocumentDetailResDto.from(teamDocument);
    }

    @Transactional
    public TeamDocumentResDto update(String email, Long teamDocumentId, TeamDocumentUpdateReqDto teamDocumentUpdateReqDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
        TeamDocument teamDocument = teamDocumentRepository.findById(teamDocumentId)
                .orElseThrow(TeamDocumentNotFoundException::new);

        teamDocument.update(
                teamDocumentUpdateReqDto.title(),
                teamDocumentUpdateReqDto.content(),
                teamDocumentUpdateReqDto.category()
        );

        return TeamDocumentResDto.from(teamDocument);
    }

    public TeamDocumentCategoriesResDto findTeamDocumentCategory(Long teamDashboardId) {
        TeamDashboard teamDashboard = teamDashboardRepository.findById(teamDashboardId)
                .orElseThrow(DashboardNotFoundException::new);

        List<String> categories = teamDocumentRepository.findTeamDocumentCategory(teamDashboard);

        return TeamDocumentCategoriesResDto.of(categories);
    }

    public FindTeamDocumentResDto findTeamDocumentByCategory(Long teamDashboardId,
                                                             String category,
                                                             Pageable pageable) {
        TeamDashboard teamDashboard = teamDashboardRepository.findById(teamDashboardId)
                .orElseThrow(DashboardNotFoundException::new);

        Page<TeamDocument> teamDocuments = teamDocumentRepository.findForTeamDocumentByCategory(teamDashboard,
                category,
                pageable);

        List<TeamDocumentResDto> teamDocumentResDtos = teamDocuments.stream()
                .map(TeamDocumentResDto::from)
                .toList();

        return FindTeamDocumentResDto.from(teamDocumentResDtos, PageInfoResDto.from(teamDocuments));
    }

    @Transactional
    public void delete(Long teamDocumentId) {
        TeamDocument teamDocument = teamDocumentRepository.findById(teamDocumentId)
                .orElseThrow(TeamDocumentNotFoundException::new);

        teamDocument.statusUpdate();
    }
}
