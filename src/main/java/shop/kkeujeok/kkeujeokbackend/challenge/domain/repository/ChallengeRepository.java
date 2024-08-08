package shop.kkeujeok.kkeujeokbackend.challenge.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;

public interface ChallengeRepository extends JpaRepository<Challenge, Long>, ChallengeCustomRepository {
}
