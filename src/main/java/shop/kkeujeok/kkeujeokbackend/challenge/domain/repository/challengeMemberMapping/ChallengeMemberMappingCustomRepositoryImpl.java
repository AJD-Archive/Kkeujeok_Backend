package shop.kkeujeok.kkeujeokbackend.challenge.domain.repository.challengeMemberMapping;

import static shop.kkeujeok.kkeujeokbackend.challenge.domain.QChallengeMemberMapping.challengeMemberMapping;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.ChallengeMemberMapping;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChallengeMemberMappingCustomRepositoryImpl implements ChallengeMemberMappingCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChallengeMemberMapping> findActiveMappings() {
        return queryFactory
                .selectFrom(challengeMemberMapping)
                .where(challengeMemberMapping.challenge.status.eq(Status.ACTIVE))
                .fetch();
    }
}
