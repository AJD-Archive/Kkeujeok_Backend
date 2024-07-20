package shop.kkeujeok.kkeujeokbackend.block.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.repository.BlockRepository;
import shop.kkeujeok.kkeujeokbackend.block.exception.BlockNotFoundException;
import shop.kkeujeok.kkeujeokbackend.block.exception.InvalidProgressException;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlockService {
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
        
        if (!block.getTitle().equals(blockUpdateReqDto.title()) ||
                !block.getContents().equals(blockUpdateReqDto.contents())) {
            block.update(blockUpdateReqDto.title(), blockUpdateReqDto.contents());
        }

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

    private Progress parseProgress(String progressString) {
        try {
            return Progress.valueOf(progressString);
        } catch (InvalidProgressException e) {
            throw new InvalidProgressException();
        }
    }

    // 블록 삭제 (논리 삭제)

    // 블록 리스트

    // 블록 상세보기

}
