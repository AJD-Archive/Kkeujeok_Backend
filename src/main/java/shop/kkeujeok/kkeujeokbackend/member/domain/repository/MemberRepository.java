package shop.kkeujeok.kkeujeokbackend.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends
        JpaRepository<Member, Long>,
        JpaSpecificationExecutor<Member>,
        MemberRepositoryCustom {
    Optional<Member> findByEmail(String email);
    boolean existsByNickname(String nickname);
}
