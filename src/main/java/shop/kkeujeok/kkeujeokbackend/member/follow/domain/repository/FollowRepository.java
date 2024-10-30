package shop.kkeujeok.kkeujeokbackend.member.follow.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowCustomRepository {
}
