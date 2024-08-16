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
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.request.FileInfoReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response.FileInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response.FileListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.File;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.repository.DocumentRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.repository.FileRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.exception.DocumentNotFoundException;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.exception.FileNotFoundException;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;

import java.util.List;
import java.util.Optional;

class FileServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    private File file;

    private Document document;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        document = Document.builder()
                .title("DocumentTitle")
                .build();

        ReflectionTestUtils.setField(document, "id", 1L);

        file = File.builder()
                .email("email")
                .title("FileTitle")
                .content("FileContent")
                .document(document)
                .build();
    }

    @DisplayName("파일을 생성합니다.")
    @Test
    void 파일을_생성합니다() {
        // given
        FileInfoReqDto fileInfoReqDto = new FileInfoReqDto(document.getId(), "email", "FileTitle", "FileContent");

        when(documentRepository.findById(anyLong())).thenReturn(Optional.of(document));
        when(fileRepository.save(any(File.class))).thenReturn(file);

        // when
        FileInfoResDto result = fileService.save("New email", fileInfoReqDto);

        // then
        assertThat(result.title()).isEqualTo(file.getTitle());
        assertThat(result.content()).isEqualTo(file.getContent());

        verify(documentRepository).findById(fileInfoReqDto.documentId());
        verify(fileRepository).save(any(File.class));
    }

    @DisplayName("존재하지 않는 문서에 파일 생성 시 예외가 발생합니다.")
    @Test
    void 존재하지_않는_문서에_파일_생성_시_예외가_발생합니다() {
        // given
        FileInfoReqDto fileInfoReqDto = new FileInfoReqDto(999L, "email", "New File", "New Content");

        when(documentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> fileService.save("noExistEmail", fileInfoReqDto))
                .isInstanceOf(DocumentNotFoundException.class);

        verify(documentRepository).findById(fileInfoReqDto.documentId());
        verify(fileRepository, never()).save(any(File.class));
    }

    @DisplayName("파일을 성공적으로 수정합니다.")
    @Test
    void 파일을_성공적으로_수정합니다() {
        // given
        FileInfoReqDto fileInfoReqDto = new FileInfoReqDto(document.getId(), "email", "Updated Title", "Updated Content");

        when(fileRepository.findById(anyLong())).thenReturn(Optional.of(file));

        // when
        FileInfoResDto result = fileService.update(1L, fileInfoReqDto);

        // then
        assertThat(result.title()).isEqualTo(fileInfoReqDto.title());
        assertThat(result.content()).isEqualTo(fileInfoReqDto.content());

        verify(fileRepository).findById(1L);
    }

    @DisplayName("존재하지 않는 파일 수정 시 예외가 발생합니다.")
    @Test
    void 존재하지_않는_파일_수정_시_예외가_발생합니다() {
        // given
        FileInfoReqDto fileInfoReqDto = new FileInfoReqDto(document.getId(), "email", "Updated Title", "Updated Content");

        when(fileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> fileService.update(1L, fileInfoReqDto))
                .isInstanceOf(FileNotFoundException.class);

        verify(fileRepository).findById(1L);
    }

    @DisplayName("파일 리스트를 성공적으로 조회합니다.")
    @Test
    void 파일_리스트를_성공적으로_조회합니다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<File> files = new PageImpl<>(List.of(file));

        when(fileRepository.findByFilesWithDocumentId(anyLong(), any(Pageable.class))).thenReturn(files);

        // when
        FileListResDto result = fileService.findForFile(1L, pageable);

        // then
        assertThat(result.fileInfoResDto().size()).isEqualTo(1);
        assertThat(result.fileInfoResDto().get(0).title()).isEqualTo(file.getTitle());

        verify(fileRepository).findByFilesWithDocumentId(1L, pageable);
    }

    @DisplayName("파일을 성공적으로 조회합니다.")
    @Test
    void 파일을_성공적으로_조회합니다() {
        // given
        when(fileRepository.findById(anyLong())).thenReturn(Optional.of(file));

        // when
        FileInfoResDto result = fileService.findById(1L);

        // then
        assertThat(result.title()).isEqualTo(file.getTitle());
        assertThat(result.content()).isEqualTo(file.getContent());

        verify(fileRepository).findById(1L);
    }

    @DisplayName("존재하지 않는 파일 조회 시 예외가 발생합니다.")
    @Test
    void 존재하지_않는_파일_조회_시_예외가_발생합니다() {
        // given
        when(fileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> fileService.findById(1L))
                .isInstanceOf(FileNotFoundException.class);

        verify(fileRepository).findById(1L);
    }

    @DisplayName("파일을 논리적으로 삭제합니다.")
    @Test
    void 파일을_논리적으로_삭제합니다() {
        // given
        when(fileRepository.findById(anyLong())).thenReturn(Optional.of(file));

        // when
        fileService.delete(1L);

        // then
        assertThat(file.getStatus()).isEqualTo(shop.kkeujeok.kkeujeokbackend.global.entity.Status.DELETED);

        verify(fileRepository).findById(1L);
    }

    @DisplayName("존재하지 않는 파일 삭제 시 예외가 발생합니다.")
    @Test
    void 존재하지_않는_파일_삭제_시_예외가_발생합니다() {
        // given
        when(fileRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> fileService.delete(1L))
                .isInstanceOf(FileNotFoundException.class);

        verify(fileRepository).findById(1L);
    }
}
