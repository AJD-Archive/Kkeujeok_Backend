package shop.kkeujeok.kkeujeokbackend.member.domain.repository;

public interface MemberCustomRepository {
    boolean existsByNicknameAndTag(String nickname, String tag);
}
