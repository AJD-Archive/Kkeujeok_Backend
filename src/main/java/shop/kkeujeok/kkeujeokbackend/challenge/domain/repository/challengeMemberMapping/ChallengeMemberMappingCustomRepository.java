package shop.kkeujeok.kkeujeokbackend.challenge.domain.repository.challengeMemberMapping;

import java.util.List;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.ChallengeMemberMapping;

public interface ChallengeMemberMappingCustomRepository {
    List<ChallengeMemberMapping> findActiveMappings();
}
