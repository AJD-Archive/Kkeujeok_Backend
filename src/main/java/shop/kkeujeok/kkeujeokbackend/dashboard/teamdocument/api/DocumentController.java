package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response.DocumentInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.request.DocumentInfoReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.application.DocumentService;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("")
    public RspTemplate<DocumentInfoResDto> save(@RequestBody DocumentInfoReqDto documentInfoReqDto) {
        return new RspTemplate<>(HttpStatus.OK, "팀 문서 등록", documentService.save(documentInfoReqDto));
    }

    @PatchMapping("/{documentId}")
    public RspTemplate<DocumentInfoResDto> update(@PathVariable(name = "documentId") Long documentId,
                                               @RequestBody DocumentInfoReqDto documentInfoReqDto) {
        return new RspTemplate<>(HttpStatus.OK, "팀 문서 수회", documentService.update(documentId,documentInfoReqDto));
    }

    // 팀 문서 조회

    // 팀 문서 삭제
    @DeleteMapping("{documentId}")
    public RspTemplate<Void> delete(@PathVariable(name = "documentId") Long documentId) {
        documentService.delete(documentId);

        return new RspTemplate<>(HttpStatus.OK, "팀 문서 삭제");
    }
}
