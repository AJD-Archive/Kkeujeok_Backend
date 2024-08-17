package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.TeamDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.team.domain.repository.TeamDashboardRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.request.DocumentInfoReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response.DocumentInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response.DocumentListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.repository.DocumentRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.exception.DocumentNotFoundException;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.exception.TeamDashboardNotFoundException;

import java.util.List;
import java.util.Optional;

class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private TeamDashboardRepository teamDashboardRepository;

    @InjectMocks
    private DocumentService documentService;

    private Document document;
    private TeamDashboard teamDashboard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        teamDashboard = TeamDashboard.builder()
                .title("Team Dashboard Title")
                .description("Team Dashboard Description")
                .build();

        ReflectionTestUtils.setField(teamDashboard, "id", 1L);

        document = Document.builder()
                .title("Document Title")
                .teamDashboard(teamDashboard)
                .build();

        ReflectionTestUtils.setField(document, "id", 1L);
    }

    @DisplayName("문서를 생성합니다.")
    @Test
    void 문서를_생성합니다() {
        // given
        DocumentInfoReqDto documentInfoReqDto = new DocumentInfoReqDto(1L, "Document Title");

        when(teamDashboardRepository.findById(anyLong())).thenReturn(Optional.of(teamDashboard));
        when(documentRepository.save(any(Document.class))).thenReturn(document);

        // when
        DocumentInfoResDto result = documentService.save(documentInfoReqDto);

        // then
        assertThat(result.title()).isEqualTo(document.getTitle());

        verify(teamDashboardRepository).findById(documentInfoReqDto.teamDashboardId());
        verify(documentRepository).save(any(Document.class));
    }

    @DisplayName("존재하지 않는 팀 대시보드에 문서를 생성 시 예외가 발생합니다.")
    @Test
    void 존재하지_않는_팀_대시보드에_문서를_생성_시_예외가_발생합니다() {
        // given
        DocumentInfoReqDto documentInfoReqDto = new DocumentInfoReqDto(999L, "Document Title");

        when(teamDashboardRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> documentService.save(documentInfoReqDto))
                .isInstanceOf(TeamDashboardNotFoundException.class);

        verify(teamDashboardRepository).findById(documentInfoReqDto.teamDashboardId());
        verify(documentRepository, never()).save(any(Document.class));
    }

    @DisplayName("문서를 성공적으로 수정합니다.")
    @Test
    void 문서를_성공적으로_수정합니다() {
        // given
        DocumentInfoReqDto documentInfoReqDto = new DocumentInfoReqDto(1L, "Updated Document Title");

        when(documentRepository.findById(anyLong())).thenReturn(Optional.of(document));

        // when
        DocumentInfoResDto result = documentService.update(1L, documentInfoReqDto);

        // then
        assertThat(result.title()).isEqualTo(documentInfoReqDto.title());

        verify(documentRepository).findById(1L);
    }

    @DisplayName("존재하지 않는 문서를 수정 시 예외가 발생합니다.")
    @Test
    void 존재하지_않는_문서를_수정_시_예외가_발생합니다() {
        // given
        DocumentInfoReqDto documentInfoReqDto = new DocumentInfoReqDto(1L, "Updated Document Title");

        when(documentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> documentService.update(1L, documentInfoReqDto))
                .isInstanceOf(DocumentNotFoundException.class);

        verify(documentRepository).findById(1L);
    }

    @DisplayName("팀 대시보드 ID로 문서를 성공적으로 조회합니다.")
    @Test
    void 팀_대시보드_ID로_문서를_성공적으로_조회합니다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Document> documents = new PageImpl<>(List.of(document));

        when(documentRepository.findByDocumentWithTeamDashboard(anyLong(), any(Pageable.class))).thenReturn(documents);

        // when
        DocumentListResDto result = documentService.findDocumentByTeamDashboardId(1L, pageable);

        // then
        assertThat(result.documentInfoResDtos().size()).isEqualTo(1);
        assertThat(result.documentInfoResDtos().get(0).title()).isEqualTo(document.getTitle());

        verify(documentRepository).findByDocumentWithTeamDashboard(1L, pageable);
    }

    @DisplayName("문서를 논리적으로 삭제합니다.")
    @Test
    void 문서를_논리적으로_삭제합니다() {
        // given
        when(documentRepository.findById(anyLong())).thenReturn(Optional.of(document));

        // when
        documentService.delete(1L);

        // then
        assertThat(document.getStatus()).isEqualTo(shop.kkeujeok.kkeujeokbackend.global.entity.Status.DELETED);

        verify(documentRepository).findById(1L);
    }

    @DisplayName("존재하지 않는 문서를 삭제 시 예외가 발생합니다.")
    @Test
    void 존재하지_않는_문서를_삭제_시_예외가_발생합니다() {
        // given
        when(documentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> documentService.delete(1L))
                .isInstanceOf(DocumentNotFoundException.class);

        verify(documentRepository).findById(1L);
    }
}
