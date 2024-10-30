package shop.kkeujeok.kkeujeokbackend.member.follow.domain.repository;

import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

public interface FollowCustomRepository {

    boolean existsByFromMemberAndToMember(Member fromMember, Member toMember);

    void acceptFollowingRequest(Long followId);
}
