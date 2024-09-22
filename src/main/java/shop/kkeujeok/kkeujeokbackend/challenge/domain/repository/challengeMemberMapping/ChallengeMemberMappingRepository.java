package shop.kkeujeok.kkeujeokbackend.challenge.domain.repository.challengeMemberMapping;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.ChallengeMemberMapping;

public interface ChallengeMemberMappingRepository extends JpaRepository<ChallengeMemberMapping, Long>,
        ChallengeMemberMappingCustomRepository {
}
