package shop.kkeujeok.kkeujeokbackend.member.follow.domain.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.FollowInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.MemberInfoForFollowResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.MyFollowsResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.RecommendedFollowInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.Follow;

public interface FollowCustomRepository {

    boolean existsByFromMemberAndToMember(Member fromMember, Member toMember);

    void acceptFollowingRequest(Long followId);

    Page<FollowInfoResDto> findFollowList(Long memberId, Pageable pageable);

    Page<RecommendedFollowInfoResDto> findRecommendedFollowList(Long memberId, Pageable pageable);

    Optional<Follow> findByFromMemberAndToMember(Member fromMember, Member toMember);

    Page<RecommendedFollowInfoResDto> searchRecommendedFollowUsingKeywords(Long memberId, String keyword, Pageable pageable);

    Page<MemberInfoForFollowResDto> searchFollowListUsingKeywords(Long memberId, String keyword, Pageable pageable);

    MyFollowsResDto findMyFollowsCount(Long memberId);
}
