package shop.kkeujeok.kkeujeokbackend.challenge.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSearchReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeInfoResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeListResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.application.ChallengeService;
import shop.kkeujeok.kkeujeokbackend.global.annotation.CurrentUserEmail;
import shop.kkeujeok.kkeujeokbackend.global.template.RspTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping
    public RspTemplate<ChallengeInfoResDto> save(@CurrentUserEmail String email,
                                                 @Valid @RequestPart ChallengeSaveReqDto challengeSaveReqDto,
                                                 @RequestPart(value = "representImage", required = false) MultipartFile representImage) {
        return new RspTemplate<>(HttpStatus.CREATED, "챌린지 생성 성공",
                challengeService.save(email, challengeSaveReqDto, representImage));
    }

    @PatchMapping("/{challengeId}")
    public RspTemplate<ChallengeInfoResDto> update(@CurrentUserEmail String email,
                                                   @PathVariable(name = "challengeId") Long challengeId,
                                                   @Valid @RequestPart ChallengeSaveReqDto challengeSaveReqDto,
                                                   @RequestPart(value = "representImage", required = false) MultipartFile representImage) {
        return new RspTemplate<>(HttpStatus.OK, "챌린지 수정 성공",
                challengeService.update(email, challengeId, challengeSaveReqDto, representImage));
    }

    @GetMapping
    public RspTemplate<ChallengeListResDto> findAllChallenges(@RequestParam(defaultValue = "0", name = "page") int page,
                                                              @RequestParam(defaultValue = "10", name = "size") int size) {
        return new RspTemplate<>(HttpStatus.OK,
                "챌린지 전체 조회 성공",
                challengeService.findAllChallenges(PageRequest.of(page, size)));
    }

    @GetMapping("/search")
    public RspTemplate<ChallengeListResDto> findChallengesByKeyWord(@RequestParam(name = "keyword") String keyWord,
                                                                    @RequestParam(defaultValue = "0", name = "page") int page,
                                                                    @RequestParam(defaultValue = "10", name = "size") int size) {
        ChallengeSearchReqDto searchReqDto = ChallengeSearchReqDto.from(keyWord);
        return new RspTemplate<>(HttpStatus.OK,
                "챌린지 검색 성공",
                challengeService.findChallengesByKeyWord(searchReqDto, PageRequest.of(page, size)));
    }

    @GetMapping("/find")
    public RspTemplate<ChallengeListResDto> findByCategory(@RequestParam(name = "category") String category,
                                                           @RequestParam(defaultValue = "0", name = "page") int page,
                                                           @RequestParam(defaultValue = "10", name = "size") int size) {
        return new RspTemplate<>(HttpStatus.OK,
                "챌린지 카테고리 검색 성공",
                challengeService.findByCategory(category, PageRequest.of(page, size)));
    }

    @GetMapping("/{challengeId}")
    public RspTemplate<ChallengeInfoResDto> findById(@PathVariable(name = "challengeId") Long challengeId) {
        return new RspTemplate<>(HttpStatus.OK, "챌린지 상세보기", challengeService.findById(challengeId));
    }

    @DeleteMapping("/{challengeId}")
    public RspTemplate<Void> delete(@CurrentUserEmail String email,
                                    @PathVariable(name = "challengeId") Long challengeId) {
        challengeService.delete(email, challengeId);
        return new RspTemplate<>(HttpStatus.OK, "챌린지 삭제 성공");
    }

    @PostMapping("/{challengeId}/{dashboardId}")
    public RspTemplate<BlockInfoResDto> addChallengeToPersonalDashboard(@CurrentUserEmail String email,
                                                                        @PathVariable(name = "challengeId") Long challengeId,
                                                                        @PathVariable(name = "dashboardId") Long personalDashboardId) {
        return new RspTemplate<>(HttpStatus.OK,
                "챌린지 참여 성공",
                challengeService.addChallengeToPersonalDashboard(email, challengeId, personalDashboardId));
    }
}
