package shop.kkeujeok.kkeujeokbackend.member.follow.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.exception.MemberNotFoundException;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.request.FollowReqDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.FollowResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.Follow;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.repository.FollowRepository;
import shop.kkeujeok.kkeujeokbackend.member.follow.exception.FollowNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @Transactional
    public FollowResDto save(String email, FollowReqDto followReqDto) {
        Member fromMember = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Member toMember = memberRepository.findById(followReqDto.memberId()).orElseThrow(FollowNotFoundException::new);

        Follow follow = followReqDto.toEntity(fromMember,toMember);

        followRepository.save(follow);

        return FollowResDto.from(toMember);
    }


    // 이미 요청했으면 거절하는 로직
    // 친구 요청 수락 로직 - 수락한 상태에서 또 요청하면 이미 친구라고 처리하는 로직
    // 친구 리스트 보는 로직
    // 추천 친구 리스트 보는 로직
    // 친구 검색 기능 (email, 혹은 닉네임#고유번호로)
    // 친구 삭제 로직
}
