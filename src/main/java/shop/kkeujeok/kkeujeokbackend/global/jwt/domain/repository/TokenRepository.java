package shop.kkeujeok.kkeujeokbackend.global.jwt.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import shop.kkeujeok.kkeujeokbackend.global.jwt.domain.Token;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    boolean existsByMember(Member member);
    Optional<Token> findByMember(Member member);
    boolean existsByRefreshToken(String refreshToken);
    Optional<Token> findByRefreshToken(String refreshToken);
}
