package shop.kkeujeok.kkeujeokbackend.member.follow.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.repository.MemberRepository;
import shop.kkeujeok.kkeujeokbackend.member.exception.MemberNotFoundException;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.request.FollowAcceptReqDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.request.FollowReqDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.FollowAcceptResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.FollowInfoListDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.FollowInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.FollowResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.Follow;
import shop.kkeujeok.kkeujeokbackend.member.follow.domain.repository.FollowRepository;
import shop.kkeujeok.kkeujeokbackend.member.follow.exception.FollowAlreadyExistsException;
import shop.kkeujeok.kkeujeokbackend.member.follow.exception.FollowNotFoundException;
import software.amazon.awssdk.services.s3.endpoints.internal.Value.Str;

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

        validateFollowDoesNotExist(fromMember, toMember);

        Follow follow = followReqDto.toEntity(fromMember, toMember);

        followRepository.save(follow);

        return FollowResDto.from(toMember);
    }

    private void validateFollowDoesNotExist(Member fromMember, Member toMember) {
        if (followRepository.existsByFromMemberAndToMember(fromMember, toMember)) {
            throw new FollowAlreadyExistsException();
        }
    }

    @Transactional
    public void accept(Long followId) {
        followRepository.acceptFollowingRequest(followId);
    }

    public FollowInfoListDto findFollowList(String email, Pageable pageable) {
        Long memberId = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new).getId();

        Page<FollowInfoResDto> followInfoResDtos = followRepository.findFollowList(memberId, pageable);

        return FollowInfoListDto.from(
                followInfoResDtos.getContent(),
                PageInfoResDto.from(followInfoResDtos)
        );
    }

//    todo
//     추천 친구 리스트 보는 로직
//     친구 검색 기능 (email, 혹은 닉네임#고유번호로)
//     친구 삭제 로직
}
