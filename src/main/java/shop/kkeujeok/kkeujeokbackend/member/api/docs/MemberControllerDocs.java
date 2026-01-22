package shop.kkeujeok.kkeujeokbackend.member.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.request.MyPageUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.MyPageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.PersonalDashboardsAndChallengesResDto;
import shop.kkeujeok.kkeujeokbackend.member.mypage.api.dto.response.TeamDashboardsAndChallengesResDto;

@Tag(name = "MemberController", description = "회원 관련 API")
public interface MemberControllerDocs {

    @Operation(summary = "내 프로필 정보 조회", description = "로그인한 사용자의 프로필 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 프로필 정보 조회 성공")
    })
    RspTemplate<MyPageInfoResDto> myProfileInfo(@CurrentUserEmail String email);

    @Operation(summary = "내 프로필 정보 수정", description = "로그인한 사용자의 프로필 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 프로필 정보 수정 성공")
    })
    RspTemplate<MyPageInfoResDto> update(@CurrentUserEmail String email,
                                         @RequestBody MyPageUpdateReqDto myPageUpdateReqDto);

    @Operation(summary = "팀 대시보드 및 챌린지 조회", description = "사용자의 팀 대시보드 및 챌린지 정보를 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팀 대시보드 및 챌린지 조회 성공")
    })
    RspTemplate<TeamDashboardsAndChallengesResDto> getTeamDashboardsAndChallenges(@CurrentUserEmail String email,
                                                                                  @RequestParam(name = "requestEmail") String requestEmail,
                                                                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                                                                  @RequestParam(name = "size", defaultValue = "10") int size);

    @Operation(summary = "친구 프로필 정보 조회", description = "특정 친구의 프로필 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구 프로필 정보 조회 성공")
    })
    RspTemplate<MyPageInfoResDto> getFriendProfileInfo(@PathVariable Long friendId);

    @Operation(summary = "친구 개인 대시보드 및 챌린지 조회", description = "친구의 개인 대시보드 및 챌린지 정보를 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구 개인 대시보드 및 챌린지 조회 성공")
    })
    RspTemplate<PersonalDashboardsAndChallengesResDto> getPersonalDashboardsAndChallenges(@PathVariable Long friendId,
                                                                                          @RequestParam(name = "page", defaultValue = "0") int page,
                                                                                          @RequestParam(name = "size", defaultValue = "10") int size);
}
