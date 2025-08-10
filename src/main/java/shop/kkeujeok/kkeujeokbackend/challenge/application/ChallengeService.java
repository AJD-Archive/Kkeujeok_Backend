package shop.kkeujeok.kkeujeokbackend.challenge.application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.Type;
import shop.kkeujeok.kkeujeokbackend.block.domain.repository.BlockRepository;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSearchReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeCompletedMemberInfoResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeInfoResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeListResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengesResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengesResDto.ChallengeSummary;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.ChallengeMemberMapping;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.repository.ChallengeRepository;
import shop.kkeujeok.kkeujeokbackend.challenge.exception.ChallengeAccessDeniedException;
import shop.kkeujeok.kkeujeokbackend.challenge.exception.ChallengeNotFoundException;
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.Dashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.exception.DashboardNotFoundException;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.repository.PersonalDashboardRepository;
import shop.kkeujeok.kkeujeokbackend.global.aws.S3Service;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.exception.MemberNotFoundException;
import shop.kkeujeok.kkeujeokbackend.notification.application.NotificationService;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private static final String CHALLENGE_CREATED_MESSAGE_TEMPLATE = "%s 챌린지 블록이 생성되었습니다.";
    private static final String CHALLENGE_JOIN_MESSAGE = "챌린지 참여: %s님이 챌린지에 참여했습니다";
    private static final String START_DATE_FORMAT = "yyyy.MM.dd HH:mm";
    private static final String DEADLINE_DATE_FORMAT = "yyyy.MM.dd 23:59";

    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;
    private final PersonalDashboardRepository personalDashboardRepository;
    private final BlockRepository blockRepository;
    private final NotificationService notificationService;
    private final S3Service s3Service;

    @Transactional
    public ChallengeInfoResDto save(String email, ChallengeSaveReqDto challengeSaveReqDto,
                                    MultipartFile representImage) {
        Member member = findMemberByEmail(email);

        String imageUrl = null;
        if (representImage != null && !representImage.isEmpty()) {
            imageUrl = s3Service.uploadChallengeImage(representImage);
        }

        Challenge challenge = challengeSaveReqDto.toEntity(member, imageUrl);
        challengeRepository.save(challenge);

        return ChallengeInfoResDto.from(challenge);
    }

    @Transactional
    public ChallengeInfoResDto update(String email, Long challengeId, ChallengeSaveReqDto challengeSaveReqDto,
                                      MultipartFile representImage) {
        Member member = findMemberByEmail(email);
        Challenge challenge = findChallengeById(challengeId);
        verifyMemberIsAuthor(challenge, member);

        String imageUrl = challenge.getRepresentImage();
        if (representImage != null && !representImage.isEmpty()) {
            imageUrl = s3Service.uploadChallengeImage(representImage);
        }

        challenge.update(
                challengeSaveReqDto.title(),
                challengeSaveReqDto.contents(),
                challengeSaveReqDto.cycle(),
                challengeSaveReqDto.cycleDetails(),
                challengeSaveReqDto.endDate(),
                challengeSaveReqDto.blockName(),
                imageUrl);

        return ChallengeInfoResDto.from(challenge);
    }

    @Transactional(readOnly = true)
    public ChallengesResDto findAllChallenges(Pageable pageable) {
        Page<ChallengeSummary> challengeSummaries = challengeRepository.findAllChallenges(pageable);

        return ChallengesResDto.of(challengeSummaries.getContent(), PageInfoResDto.from(challengeSummaries));
    }

    @Transactional(readOnly = true)
    public ChallengeInfoResDto findById(String email, Long challengeId) {
        Member member = findMemberByEmail(email);
        Challenge challenge = findChallengeById(challengeId);

        boolean isParticipant = checkIfParticipant(challenge, member);
        boolean isAuthor = member.equals(challenge.getMember());

        Set<ChallengeCompletedMemberInfoResDto> completedMembers = getCompletedMembers(challenge);

        return ChallengeInfoResDto.of(challenge, isParticipant, isAuthor, completedMembers);
    }

    private boolean checkIfParticipant(Challenge challenge, Member member) {
        return challenge.getParticipants().stream()
                .anyMatch(mapping -> mapping.getMember().equals(member));
    }

    private Set<ChallengeCompletedMemberInfoResDto> getCompletedMembers(Challenge challenge) {
        return challenge.getParticipants().stream()
                .filter(ChallengeMemberMapping::isCompleted)
                .map(mapping -> ChallengeCompletedMemberInfoResDto.from(mapping.getMember()))
                .collect(Collectors.toSet());
    }

    @Transactional
    public void delete(String email, Long challengeId) {
        Member member = findMemberByEmail(email);
        Challenge challenge = findChallengeById(challengeId);
        verifyMemberIsAuthor(challenge, member);

        Set<ChallengeMemberMapping> challengeMemberMappings = challenge.getParticipants();
        challenge.getParticipants().removeAll(challengeMemberMappings);

        challenge.updateStatus();
    }

    @Transactional
    public void addChallengeToPersonalDashboard(String email, Long challengeId, Long personalDashboardId) {
        Member member = findMemberByEmail(email);
        Challenge challenge = findChallengeById(challengeId);
        PersonalDashboard personalDashboard = personalDashboardRepository.findById(personalDashboardId)
                .orElseThrow(DashboardNotFoundException::new);

        challenge.addParticipant(member, personalDashboard);
        createBlockIfActiveToday(challenge, member, personalDashboard);

        challengeRepository.save(challenge);

        String challengeJoinMessage = String.format(CHALLENGE_JOIN_MESSAGE, member.getName());
        notificationService.sendNotification(challenge.getMember(), challengeJoinMessage);

        String challengeCreateMessage = String.format(CHALLENGE_CREATED_MESSAGE_TEMPLATE, challenge.getBlockName());
        notificationService.sendNotification(member, challengeCreateMessage);
    }

    private void createBlockIfActiveToday(Challenge challenge, Member member, Dashboard personalDashboard) {
        if (challenge.isActiveToday()) {
            Block block = createBlock(challenge, member, personalDashboard);
            blockRepository.save(block);
        }
    }

    private Block createBlock(Challenge challenge, Member member, Dashboard personalDashboard) {
        return Block.builder()
                .title(challenge.getTitle())
                .contents(generateCycleDescription(challenge))
                .progress(Progress.NOT_STARTED)
                .type(Type.CHALLENGE)
                .startDate(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern(START_DATE_FORMAT)))
                .deadLine(LocalDateTime.now()
                        .withHour(23).withMinute(59)
                        .format(DateTimeFormatter.ofPattern(DEADLINE_DATE_FORMAT)))
                .member(member)
                .dashboard(personalDashboard)
                .challenge(challenge)
                .build();
    }

    private String generateCycleDescription(Challenge challenge) {
        return challenge.getCycle().getDescription() + " " +
                challenge.getCycleDetails().stream()
                        .map(CycleDetail::getDescription)
                        .collect(Collectors.joining(", "));
    }

    @Transactional(readOnly = true)
    public ChallengeListResDto findChallengeForMemberId(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        Page<ChallengeInfoResDto> challenges = challengeRepository.findChallengesByMemberInMapping(member, pageable);

        return ChallengeListResDto.of(challenges.getContent(), PageInfoResDto.from(challenges));
    }

    @Transactional(readOnly = true)
    public ChallengesResDto findChallengesByCategoryAndKeyword(ChallengeSearchReqDto challengeSearchReqDto,
                                                               Pageable pageable) {
        Page<ChallengeSummary> challengeSummaries = challengeRepository.findChallengesByCategoryAndKeyword(
                challengeSearchReqDto, pageable);

        return ChallengesResDto.of(challengeSummaries.getContent(), PageInfoResDto.from(challengeSummaries));
    }

    @Transactional
    public void withdrawFromChallenge(String email, Long challengeId) {
        Member member = findMemberByEmail(email);
        Challenge challenge = findChallengeById(challengeId);

        ChallengeMemberMapping mapping = challenge.getParticipants().stream()
                .filter(participant -> participant.getMember().equals(member))
                .findFirst()
                .orElseThrow(() -> new ChallengeAccessDeniedException("이 챌린지에 참여하지 않았습니다."));

        challenge.removeParticipant(mapping);
        challengeRepository.save(challenge);
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
