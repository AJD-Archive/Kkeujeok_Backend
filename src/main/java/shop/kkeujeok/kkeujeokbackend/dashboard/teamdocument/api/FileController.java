package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.request.FileInfoReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response.FileInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response.FileListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.application.FileService;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("")
    public RspTemplate<FileInfoResDto> save(@CurrentUserEmail String email,
                                            @RequestBody FileInfoReqDto fileInfoReqDto) {
        return new RspTemplate<>(HttpStatus.OK, "팀 파일 등록", fileService.save(email, fileInfoReqDto));
    }

    @PatchMapping("/{fileId}")
    public RspTemplate<FileInfoResDto> update(@PathVariable(name = "fileId") Long fileId,
                                              @RequestBody FileInfoReqDto fileInfoReqDto) {
        return new RspTemplate<>(HttpStatus.OK, "팀 파일 수정", fileService.update(fileId, fileInfoReqDto));
    }

    @GetMapping("")
    public RspTemplate<FileListResDto> findForFile(@RequestParam(name = "documentId") Long documentId,
                                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "size", defaultValue = "10") int size) {
        return new RspTemplate<>(HttpStatus.OK,
                "팀 문서 파일 조회",
                fileService.findForFile(documentId, PageRequest.of(page, size)));
    }

    @GetMapping("/{fileId}")
    public RspTemplate<FileInfoResDto> findById(@PathVariable(name = "fileId") Long fileId) {
        return new RspTemplate<>(HttpStatus.OK, "팀 파일 상세보기", fileService.findById(fileId));
    }

    @DeleteMapping("{fileId}")
    public RspTemplate<Void> delete(@PathVariable(name = "fileId") Long blockId) {
        fileService.delete(blockId);

        return new RspTemplate<>(HttpStatus.OK, "팀 파일 삭제");
    }
}
