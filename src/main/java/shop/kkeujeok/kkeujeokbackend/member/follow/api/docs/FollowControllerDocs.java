package shop.kkeujeok.kkeujeokbackend.member.follow.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.request.FollowReqDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.FollowInfoListDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.FollowResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.MemberInfoForFollowListDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.MyFollowsResDto;
import shop.kkeujeok.kkeujeokbackend.member.follow.api.dto.response.RecommendedFollowInfoListDto;

@Tag(name = "FollowController", description = "친구(팔로우) 관련 API")
public interface FollowControllerDocs {

    @Operation(summary = "친구 추가 요청", description = "다른 사용자에게 친구 추가를 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구 추가 요청 성공")
    })
    RspTemplate<FollowResDto> save(@CurrentUserEmail String email,
                                   @RequestBody FollowReqDto followReqDto);

    @Operation(summary = "친구 추가 수락", description = "받은 친구 추가 요청을 수락합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구 추가 수락 성공")
    })
    RspTemplate<Void> accept(@CurrentUserEmail String email,
                             @PathVariable Long followId);

    @Operation(summary = "내 친구 목록 조회", description = "사용자의 친구 목록을 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 친구 목록 조회 성공")
    })
    RspTemplate<FollowInfoListDto> findFollowList(@CurrentUserEmail String email,
                                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "10") int size);

    @Operation(summary = "추천 친구 목록 조회", description = "사용자에게 추천할 친구 목록을 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추천 친구 목록 조회 성공")
    })
    RspTemplate<RecommendedFollowInfoListDto> findRecommendedFollowList(@CurrentUserEmail String email,
                                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                                        @RequestParam(name = "size", defaultValue = "8") int size);

    @Operation(summary = "친구 삭제", description = "특정 친구를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구 삭제 성공")
    })
    RspTemplate<Void> delete(@CurrentUserEmail String email, @PathVariable Long memberId);

    @Operation(summary = "키워드로 전체 친구 조회", description = "키워드를 사용하여 전체 사용자를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "키워드로 전체 친구 조회 성공")
    })
    RspTemplate<MemberInfoForFollowListDto> searchFollowListUsingKeywords(@CurrentUserEmail String email,
                                                                          @RequestParam(name = "keyword") String keyword,
                                                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                                                          @RequestParam(name = "size", defaultValue = "10") int size);

    @Operation(summary = "내 팔로우 수 조회", description = "사용자의 팔로우 수를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 팔로우 수 조회 성공")
    })
    RspTemplate<MyFollowsResDto> findMyFollowsCount(@CurrentUserEmail String email);
}
