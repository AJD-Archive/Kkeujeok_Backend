package shop.kkeujeok.kkeujeokbackend.block.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockListResDto;
import shop.kkeujeok.kkeujeokbackend.block.application.BlockService;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blocks")
public class BlockController {
    private final BlockService blockService;

    @PostMapping("/")
    public RspTemplate<BlockInfoResDto> save(@RequestBody BlockSaveReqDto blockSaveReqDto) {
        return new RspTemplate<>(HttpStatus.CREATED, "블럭 생성", blockService.save(blockSaveReqDto));
    }

    @PatchMapping("/{blockId}")
    public RspTemplate<BlockInfoResDto> update(@PathVariable(name = "blockId") Long blockId,
                                               @RequestBody BlockUpdateReqDto blockUpdateReqDto) {
        return new RspTemplate<>(HttpStatus.OK, "블록 수정", blockService.update(blockId, blockUpdateReqDto));
    }

    @PatchMapping("/{blockId}/progress")
    public RspTemplate<BlockInfoResDto> progressUpdate(@PathVariable(name = "blockId") Long blockId,
                                                       @RequestParam(name = "progress") String progress) {
        return new RspTemplate<>(HttpStatus.OK, "블록 상태 수정", blockService.progressUpdate(blockId, progress));
    }

    @GetMapping("")
    public RspTemplate<BlockListResDto> findByBlockWithProgress(@RequestParam(name = "progress") String progress,
                                                                @RequestParam(name = "page", defaultValue = "0") int page,
                                                                @RequestParam(name = "size", defaultValue = "10") int size) {
        return new RspTemplate<>(HttpStatus.OK,
                "블록 상태별 전체 조회",
                blockService.findByBlockWithProgress(progress, PageRequest.of(page, size)));
    }

    @DeleteMapping("/{blockId}")
    public RspTemplate<Void> delete(@PathVariable(name = "blockId") Long blockId) {
        blockService.delete(blockId);
        return new RspTemplate<>(HttpStatus.OK, "블록 삭제");
    }

    @GetMapping("/{blockId}")
    public RspTemplate<BlockInfoResDto> findById(@PathVariable(name = "blockId") Long blockId) {
        return new RspTemplate<>(HttpStatus.OK, "블록 상세보기", blockService.findById(blockId));
    }

}
