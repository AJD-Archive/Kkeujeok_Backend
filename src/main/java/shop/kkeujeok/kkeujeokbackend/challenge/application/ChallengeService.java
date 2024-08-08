package shop.kkeujeok.kkeujeokbackend.challenge.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSearchReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeInfoResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeListResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.repository.ChallengeRepository;
import shop.kkeujeok.kkeujeokbackend.challenge.exception.ChallengeAccessDeniedException;
import shop.kkeujeok.kkeujeokbackend.challenge.exception.ChallengeNotFoundException;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.exception.MemberNotFoundException;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;

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

    @Transactional
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

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Challenge findChallengeById(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(ChallengeNotFoundException::new);
    }


    private void verifyMemberIsAuthor(Challenge challenge, Member currentUser) {
        if (!challenge.getMember().equals(currentUser)) {
            throw new ChallengeAccessDeniedException();
        }
    }
}
