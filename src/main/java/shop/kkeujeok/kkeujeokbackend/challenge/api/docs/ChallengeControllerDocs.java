package shop.kkeujeok.kkeujeokbackend.challenge.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeInfoResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengesResDto;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

@Tag(name = "ChallengeController", description = "챌린지 관련 API")
public interface ChallengeControllerDocs {

    @Operation(summary = "챌린지 생성", description = "새로운 챌린지를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "챌린지 생성 성공")
    })
    RspTemplate<ChallengeInfoResDto> save(@CurrentUserEmail String email,
                                          @Valid @RequestPart ChallengeSaveReqDto challengeSaveReqDto,
                                          @RequestPart(value = "representImage", required = false) MultipartFile representImage);

    @Operation(summary = "챌린지 수정", description = "기존 챌린지를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 수정 성공")
    })
    RspTemplate<ChallengeInfoResDto> update(@CurrentUserEmail String email,
                                            @PathVariable(name = "challengeId") Long challengeId,
                                            @Valid @RequestPart ChallengeSaveReqDto challengeSaveReqDto,
                                            @RequestPart(value = "representImage", required = false) MultipartFile representImage);

    @Operation(summary = "챌린지 전체 조회", description = "모든 챌린지를 페이징하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 전체 조회 성공")
    })
    RspTemplate<ChallengesResDto> findAllChallenges(@RequestParam(defaultValue = "0", name = "page") int page,
                                                    @RequestParam(defaultValue = "10", name = "size") int size);

    @Operation(summary = "챌린지 상세보기", description = "특정 챌린지의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 상세보기 성공")
    })
    RspTemplate<ChallengeInfoResDto> findById(@CurrentUserEmail String email,
                                              @PathVariable(name = "challengeId") Long challengeId);

    @Operation(summary = "챌린지 삭제", description = "특정 챌린지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 삭제 성공")
    })
    RspTemplate<Void> delete(@CurrentUserEmail String email,
                             @PathVariable(name = "challengeId") Long challengeId);

    @Operation(summary = "챌린지 참여", description = "특정 챌린지를 개인 대시보드에 추가하여 참여합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 참여 성공")
    })
    RspTemplate<Void> addChallengeToPersonalDashboard(@CurrentUserEmail String email,
                                                      @PathVariable(name = "challengeId") Long challengeId,
                                                      @PathVariable(name = "dashboardId") Long personalDashboardId);

    @Operation(summary = "챌린지 검색", description = "카테고리와 키워드로 챌린지를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 검색 성공")
    })
    RspTemplate<ChallengesResDto> findChallengesByCategoryAndKeyword(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size);

    @Operation(summary = "챌린지 탈퇴", description = "참여 중인 챌린지에서 탈퇴합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 탈퇴 성공")
    })
    RspTemplate<Void> withdrawFromChallenge(@CurrentUserEmail String email,
                                            @PathVariable(name = "challengeId") Long challengeId);
}
