package shop.kkeujeok.kkeujeokbackend.block.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
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
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.Dashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.repository.DashboardRepository;
import shop.kkeujeok.kkeujeokbackend.dashboard.exception.DashboardNotFoundException;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.exception.MemberNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlockService {

    private final MemberRepository memberRepository;
    private final BlockRepository blockRepository;
    private final DashboardRepository dashboardRepository;

    // 블록 생성
    @Transactional
    public BlockInfoResDto save(String email, BlockSaveReqDto blockSaveReqDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Dashboard dashboard = dashboardRepository.findById(blockSaveReqDto.dashboardId())
                .orElseThrow(DashboardNotFoundException::new);
        Block block = blockRepository.save(blockSaveReqDto.toEntity(member, dashboard));

        return BlockInfoResDto.from(block);
    }

    // 블록 수정 (자동 수정 예정)
    @Transactional
    public BlockInfoResDto update(String email, Long blockId, BlockUpdateReqDto blockUpdateReqDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Block block = blockRepository.findById(blockId).orElseThrow(BlockNotFoundException::new);

        block.update(blockUpdateReqDto.title(),
                blockUpdateReqDto.contents(),
                blockUpdateReqDto.startDate(),
                blockUpdateReqDto.deadLine());

        return BlockInfoResDto.from(block);
    }

    // 블록 상태 업데이트 (Progress)
    @Transactional
    public BlockInfoResDto progressUpdate(String email, Long blockId, String progressString) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Block block = blockRepository.findById(blockId).orElseThrow(BlockNotFoundException::new);

        Progress progress = parseProgress(progressString);

        block.progressUpdate(progress);

        return BlockInfoResDto.from(block);
    }

    // 블록 리스트
    public BlockListResDto findForBlockByProgress(String email, Long dashboardId, String progress, Pageable pageable) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Page<Block> blocks = blockRepository.findByBlockWithProgress(dashboardId, parseProgress(progress), pageable);

        List<BlockInfoResDto> blockInfoResDtoList = blocks.stream()
                .map(BlockInfoResDto::from)
                .toList();

        return BlockListResDto.from(blockInfoResDtoList, PageInfoResDto.from(blocks));
    }

    // 블록 상세보기
    public BlockInfoResDto findById(String email, Long blockId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Block block = blockRepository.findById(blockId).orElseThrow(BlockNotFoundException::new);

        return BlockInfoResDto.from(block);
    }

    // 블록 삭제 유무 업데이트 (논리 삭제)
    @Transactional
    public void delete(String email, Long blockId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
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
