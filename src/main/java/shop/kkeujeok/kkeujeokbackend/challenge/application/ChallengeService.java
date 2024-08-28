package shop.kkeujeok.kkeujeokbackend.challenge.application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.Type;
import shop.kkeujeok.kkeujeokbackend.block.domain.repository.BlockRepository;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSearchReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeInfoResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeListResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.application.util.ChallengeBlockStatusUtil;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.repository.ChallengeRepository;
import shop.kkeujeok.kkeujeokbackend.challenge.exception.ChallengeAccessDeniedException;
import shop.kkeujeok.kkeujeokbackend.challenge.exception.ChallengeNotFoundException;
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.Dashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.exception.DashboardNotFoundException;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.repository.PersonalDashboardRepository;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.exception.MemberNotFoundException;
import shop.kkeujeok.kkeujeokbackend.notification.application.NotificationService;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;
    private final PersonalDashboardRepository personalDashboardRepository;
    private final BlockRepository blockRepository;
    private final NotificationService notificationService;

    @Transactional
    public ChallengeInfoResDto save(String email, ChallengeSaveReqDto challengeSaveReqDto) {
        Member member = findMemberByEmail(email);
        Challenge challenge = challengeSaveReqDto.toEntity(member);

        challengeRepository.save(challenge);

        return ChallengeInfoResDto.from(challenge);
    }

    @Transactional
    public ChallengeInfoResDto update(String email, Long challengeId, ChallengeSaveReqDto challengeSaveReqDto) {
        Member member = findMemberByEmail(email);
        Challenge challenge = findChallengeById(challengeId);
        verifyMemberIsAuthor(challenge, member);

        challenge.update(challengeSaveReqDto.title(),
                challengeSaveReqDto.contents(),
                challengeSaveReqDto.cycleDetails(),
                challengeSaveReqDto.startDate(),
                challengeSaveReqDto.endDate(),
                challengeSaveReqDto.representImage());

        return ChallengeInfoResDto.from(challenge);
    }

    @Transactional(readOnly = true)
    public ChallengeListResDto findAllChallenges(Pageable pageable) {
        Page<Challenge> challenges = challengeRepository.findAllChallenges(pageable);

        List<ChallengeInfoResDto> challengeInfoResDtoList = challenges.stream()
                .map(ChallengeInfoResDto::from)
                .toList();

        return ChallengeListResDto.of(challengeInfoResDtoList, PageInfoResDto.from(challenges));
    }

    @Transactional(readOnly = true)
    public ChallengeListResDto findChallengesByKeyWord(ChallengeSearchReqDto challengeSearchReqDto,
                                                       Pageable pageable) {
        Page<Challenge> challenges = challengeRepository.findChallengesByKeyWord(challengeSearchReqDto, pageable);

        List<ChallengeInfoResDto> challengeInfoResDtoList = challenges.stream()
                .map(ChallengeInfoResDto::from)
                .toList();

        return ChallengeListResDto.of(challengeInfoResDtoList, PageInfoResDto.from(challenges));
    }

    @Transactional(readOnly = true)
    public ChallengeListResDto findByCategory(String category, Pageable pageable) {
        Page<Challenge> challenges = challengeRepository.findChallengesByCategory(category, pageable);

        List<ChallengeInfoResDto> challengeInfoResDtoList = challenges.stream()
                .map(ChallengeInfoResDto::from)
                .toList();

        return ChallengeListResDto.of(challengeInfoResDtoList, PageInfoResDto.from(challenges));
    }

    @Transactional(readOnly = true)
    public ChallengeInfoResDto findById(Long challengeId) {
        Challenge challenge = findChallengeById(challengeId);

        return ChallengeInfoResDto.from(challenge);
    }

    @Transactional
    public void delete(String email, Long challengeId) {
        Member member = findMemberByEmail(email);
        Challenge challenge = findChallengeById(challengeId);
        verifyMemberIsAuthor(challenge, member);

        challenge.updateStatus();
    }

    @Transactional
    public BlockInfoResDto addChallengeToPersonalDashboard(String email, Long personalDashboardId, Long challengeId) {
        Member member = findMemberByEmail(email);
        Challenge challenge = findChallengeById(challengeId);
        Dashboard personalDashboard = personalDashboardRepository.findById(personalDashboardId)
                .orElseThrow(DashboardNotFoundException::new);

        Block block = createBlock(challenge, member, personalDashboard);
        updateBlockStatusIfNotActive(block, challenge);

        blockRepository.save(block);

        String message = String.format("%s님이 챌린지에 참여했습니다", member.getName());
        notificationService.sendNotification(challenge.getMember(), message);

        return BlockInfoResDto.from(block);
    }

    @Transactional(readOnly = true)
    public ChallengeListResDto findChallengeForMemberId(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        Page<Challenge> challenges = challengeRepository.findChallengesByEmail(member, pageable);

        List<ChallengeInfoResDto> challengeInfoResDtoList = challenges.stream()
                .map(ChallengeInfoResDto::from)
                .collect(Collectors.toList());

        return ChallengeListResDto.of(challengeInfoResDtoList, PageInfoResDto.from(challenges));
    }

    private Block createBlock(Challenge challenge, Member member, Dashboard personalDashboard) {
        return Block.builder()
                .title(challenge.getTitle())
                .contents(challenge.getContents())
                .progress(Progress.NOT_STARTED)
                .type(Type.CHALLENGE)
                .deadLine(LocalDate.now()
                        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd 23:59")))
                .member(member)
                .dashboard(personalDashboard)
                .challenge(challenge)
                .build();
    }

    private void updateBlockStatusIfNotActive(Block block, Challenge challenge) {
        if (!ChallengeBlockStatusUtil.isChallengeBlockActiveToday(challenge.getCycle(), challenge.getCycleDetails())) {
            block.updateChallengeStatus(Status.UN_ACTIVE);
        }
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Challenge findChallengeById(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(ChallengeNotFoundException::new);
    }


    private void verifyMemberIsAuthor(Challenge challenge, Member member) {
        if (!challenge.getMember().equals(member)) {
            throw new ChallengeAccessDeniedException();
        }
    }
}
