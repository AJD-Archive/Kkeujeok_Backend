package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.repository.TeamDashboardRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.request.DocumentUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response.DocumentListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.exception.DocumentNotFoundException;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response.DocumentInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.request.DocumentInfoReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.repository.DocumentRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.exception.TeamDashboardNotFoundException;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.exception.MemberNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final TeamDashboardRepository teamDashboardRepository;

    // 팀 문서 생성 , 팀 대시보드 연관관계 설정 필요
    @Transactional
    public DocumentInfoResDto save(DocumentInfoReqDto documentInfoReqDto) {
        TeamDashboard teamDashboard = teamDashboardRepository.findById(documentInfoReqDto.teamDashboardId())
                .orElseThrow(TeamDashboardNotFoundException::new);
        Document document = documentInfoReqDto.toEntity(teamDashboard);

        documentRepository.save(document);

        return DocumentInfoResDto.from(document);
    }

    // 팀 문서 수정
    @Transactional
    public DocumentInfoResDto update(Long documentId, DocumentUpdateReqDto documentUpdateReqDto) {
        Document document = documentRepository.findById(documentId).orElseThrow(DocumentNotFoundException::new);

        document.update(documentUpdateReqDto.title());

        return DocumentInfoResDto.from(document);
    }

    // 팀 문서 조회 팀 대시보드 아이디로 조회
    public DocumentListResDto findDocumentByTeamDashboardId(Long teamDashboardId, Pageable pageable) {
        Page<Document> documents = documentRepository.findByDocumentWithTeamDashboard(teamDashboardId, pageable);

        List<DocumentInfoResDto> documentInfoResDtoList = documents.stream()
                .map(DocumentInfoResDto::from)
                .toList();

        return DocumentListResDto.from(documentInfoResDtoList, PageInfoResDto.from(documents));
    }

    // 팀 문서 삭제
    @Transactional
    public void delete(Long documentId) {
        Document document = documentRepository.findById(documentId).orElseThrow(DocumentNotFoundException::new);

        document.statusUpdate();
    }
}
