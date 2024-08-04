package shop.kkeujeok.kkeujeokbackend.challenge.domain.repository;

import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
