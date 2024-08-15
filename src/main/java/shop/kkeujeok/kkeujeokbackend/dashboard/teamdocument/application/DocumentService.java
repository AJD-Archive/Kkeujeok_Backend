package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response.DocumentListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.exception.DocumentNotFoundException;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response.DocumentInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.request.DocumentInfoReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.repository.DocumentRepository;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.exception.MemberNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentService {

    private final DocumentRepository documentRepository;

    // 팀 문서 생성 , 팀 대시보드 연관관계 설정 필요
    @Transactional
    public DocumentInfoResDto save(DocumentInfoReqDto documentInfoReqDto) {
        Document document = documentInfoReqDto.toEntity();

        documentRepository.save(document);

        return DocumentInfoResDto.from(document);
    }

    // 팀 문서 수정
    @Transactional
    public DocumentInfoResDto update(Long documentId, DocumentInfoReqDto documentInfoReqDto) {
        Document document = documentRepository.findById(documentId).orElseThrow(DocumentNotFoundException::new);

        document.update(documentInfoReqDto.title());

        return DocumentInfoResDto.from(document);
    }

    // 팀 문서 조회 팀 대시보드 아이디로 조회
//    public DocumentListResDto findDocumentByTeamDashboardId(Long TeamDashboardId) {
//        DocumentListResDto documentListResDtos = DocumentListResDto.from()
//    }

    // 팀 문서 삭제
    @Transactional
    public void delete(Long documentId) {
        Document document = documentRepository.findById(documentId).orElseThrow(DocumentNotFoundException::new);

        document.statusUpdate();
    }
}
