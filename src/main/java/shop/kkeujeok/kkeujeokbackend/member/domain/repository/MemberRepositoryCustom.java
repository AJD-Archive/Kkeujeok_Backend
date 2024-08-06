package shop.kkeujeok.kkeujeokbackend.member.domain.repository;

public interface MemberRepositoryCustom {
    boolean existsByNicknameAndTag(String nickname, String tag);
}
