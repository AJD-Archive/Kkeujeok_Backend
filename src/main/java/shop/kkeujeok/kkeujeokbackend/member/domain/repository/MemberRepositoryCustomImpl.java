package shop.kkeujeok.kkeujeokbackend.member.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import shop.kkeujeok.kkeujeokbackend.member.domain.QMember;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByNicknameAndTag(String nickname, String tag) {
        QMember member = QMember.member;

        return queryFactory.selectFrom(member)
                .where(member.nickname.eq(nickname)
                        .and(member.tag.eq(tag)))
                .fetchFirst() != null;
    }
}
