package shop.kkeujeok.kkeujeokbackend.block.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
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
}
