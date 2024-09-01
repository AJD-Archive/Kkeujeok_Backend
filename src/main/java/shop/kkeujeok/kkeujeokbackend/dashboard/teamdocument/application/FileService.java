package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final DocumentRepository documentRepository;
    private final FileRepository fileRepository;

    // 팀 파일 생성
    @Transactional
    public FileInfoResDto save(String email, FileInfoReqDto fileInfoReqDto) {
        Document document = documentRepository.findById(fileInfoReqDto.documentId())
                .orElseThrow(DocumentNotFoundException::new);
        File file = fileInfoReqDto.toEntity(email, document);

        fileRepository.save(file);

        return FileInfoResDto.from(file);
    }

    // 팀 파일 수정
    @Transactional
    public FileInfoResDto update(Long fileID, FileInfoReqDto fileInfoReqDto) {
        File file = fileRepository.findById(fileID).orElseThrow(FileNotFoundException::new);

        file.update(fileInfoReqDto.title(), fileInfoReqDto.content());

        return FileInfoResDto.from(file);
    }

    // 팀 파일 리스트 조회
    public FileListResDto findForFile(Long documentId, Pageable pageable) {
        Page<File> files = fileRepository.findByFilesWithDocumentId(documentId, pageable);

        List<FileInfoResDto> fileInfoResDtoList = files.stream()
                .map(FileInfoResDto::from)
                .toList();

        return FileListResDto.of(fileInfoResDtoList, PageInfoResDto.from(files));
    }

    // 팀 파일 상세보기
    public FileInfoResDto findById(Long fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(FileNotFoundException::new);

        return FileInfoResDto.from(file);
    }

    // 팀 파일 삭제
    @Transactional
    public void delete(Long fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(FileNotFoundException::new);

        file.statusUpdate();
    }
}
