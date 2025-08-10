package shop.kkeujeok.kkeujeokbackend.challenge.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSearchReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeInfoResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengesResDto.ChallengeSummary;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

public interface ChallengeCustomRepository {
    Page<ChallengeSummary> findAllChallenges(Pageable pageable);

    Page<ChallengeInfoResDto> findChallengesByMemberInMapping(Member member, Pageable pageable);

    Page<ChallengeSummary> findChallengesByCategoryAndKeyword(ChallengeSearchReqDto challengeSearchReqDto,
                                                              Pageable pageable);
}
