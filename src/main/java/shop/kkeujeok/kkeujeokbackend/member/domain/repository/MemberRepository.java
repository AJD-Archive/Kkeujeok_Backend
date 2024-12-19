package shop.kkeujeok.kkeujeokbackend.member.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

public interface MemberRepository extends
        JpaRepository<Member, Long>,
        JpaSpecificationExecutor<Member>,
        MemberCustomRepository {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByNicknameAndTag(String nickname, String tag);

    boolean existsByNickname(String nickname);
}
