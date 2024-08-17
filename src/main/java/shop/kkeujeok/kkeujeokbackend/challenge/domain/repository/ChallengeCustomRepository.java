package shop.kkeujeok.kkeujeokbackend.challenge.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSearchReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;

public interface ChallengeCustomRepository {
    Page<Challenge> findAllChallenges(Pageable pageable);

    Page<Challenge> findChallengesByKeyWord(ChallengeSearchReqDto challengeSearchReqDto, Pageable pageable);
}
