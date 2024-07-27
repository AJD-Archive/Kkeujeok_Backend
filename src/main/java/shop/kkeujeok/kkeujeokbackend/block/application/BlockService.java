package shop.kkeujeok.kkeujeokbackend.block.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockListResDto;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.repository.BlockRepository;
import shop.kkeujeok.kkeujeokbackend.block.exception.BlockNotFoundException;
import shop.kkeujeok.kkeujeokbackend.block.exception.InvalidProgressException;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlockService {
    private static final Logger log = LoggerFactory.getLogger(BlockService.class);
    private final MemberRepository memberRepository;
    private final BlockRepository blockRepository;

    // 블록 생성
    @Transactional
    public BlockInfoResDto save(BlockSaveReqDto blockSaveReqDto) {
        // 로그인/회원가입 코드 완성 후 사용자 정보 받아올 예정
        Member member = Member.builder().nickname("member").build();
        Block block = blockRepository.save(blockSaveReqDto.toEntity(member));

        return BlockInfoResDto.of(block, member);
    }

    // 블록 수정 (자동 수정 예정)
    @Transactional
    public BlockInfoResDto update(Long blockId, BlockUpdateReqDto blockUpdateReqDto) {
        // 로그인/회원가입 코드 완성 후 사용자 정보 받아올 예정
        Member member = Member.builder().nickname("member").build();
        Block block = blockRepository.findById(blockId).orElseThrow(BlockNotFoundException::new);

        block.update(blockUpdateReqDto.title(), blockUpdateReqDto.contents(), blockUpdateReqDto.deadLine());

        return BlockInfoResDto.of(block, member);
    }

    // 블록 상태 업데이트 (Progress)
    @Transactional
    public BlockInfoResDto progressUpdate(Long blockId, String progressString) {
        // 로그인/회원가입 코드 완성 후 사용자 정보 받아올 예정
        Member member = Member.builder().nickname("member").build();
        Block block = blockRepository.findById(blockId).orElseThrow(BlockNotFoundException::new);

        Progress progress = parseProgress(progressString);

        block.progressUpdate(progress);

        return BlockInfoResDto.of(block, member);
    }

    // 블록 리스트
    public BlockListResDto findByBlockWithProgress(String progress, Pageable pageable) {
        Member member = Member.builder().nickname("member").build();
        Page<Block> blocks = blockRepository.findByBlockWithProgress(parseProgress(progress), pageable);

        List<BlockInfoResDto> blockInfoResDtoList = blocks.stream()
                .map(b -> BlockInfoResDto.of(b, member))
                .toList();

        return BlockListResDto.from(blockInfoResDtoList, PageInfoResDto.from(blocks));
    }

    // 블록 상세보기
    public BlockInfoResDto findById(Long blockId) {
        // 로그인/회원가입 코드 완성 후 사용자 정보 받아올 예정
        Member member = Member.builder().nickname("member").build();
        Block block = blockRepository.findById(blockId).orElseThrow(BlockNotFoundException::new);

        return BlockInfoResDto.of(block, member);
    }

    // 블록 삭제 유무 업데이트 (논리 삭제)
    @Transactional
    public void delete(Long blockId) {
        // 로그인/회원가입 코드 완성 후 사용자 정보 받아올 예정
        Member member = Member.builder().nickname("member").build();
        Block block = blockRepository.findById(blockId).orElseThrow(BlockNotFoundException::new);

        block.statusUpdate();
    }

    private Progress parseProgress(String progressString) {
        try {
            return Progress.valueOf(progressString);
        } catch (IllegalArgumentException e) {
            throw new InvalidProgressException();
        }
    }

}
