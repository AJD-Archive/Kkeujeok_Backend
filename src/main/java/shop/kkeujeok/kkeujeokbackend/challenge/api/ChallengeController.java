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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
                                                 @Valid @RequestBody ChallengeSaveReqDto challengeSaveReqDto) {
        return new RspTemplate<>(HttpStatus.CREATED, "챌린지 생성 성공", challengeService.save(email, challengeSaveReqDto));
    }

    @PatchMapping("/{challenge-id}")
    public RspTemplate<ChallengeInfoResDto> update(@CurrentUserEmail String email,
                                                   @PathVariable(name = "challenge-id") Long challengeId,
                                                   @Valid @RequestBody ChallengeSaveReqDto challengeSaveReqDto) {
        return new RspTemplate<>(HttpStatus.OK, "챌린지 수정 성공",
                challengeService.update(email, challengeId, challengeSaveReqDto));
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

    @GetMapping("/{challenge-id}")
    public RspTemplate<ChallengeInfoResDto> findById(@PathVariable(name = "challenge-id") Long challengeId) {
        return new RspTemplate<>(HttpStatus.OK, "챌린지 상세보기", challengeService.findById(challengeId));
    }

    @DeleteMapping("/{challenge-id}")
    public RspTemplate<Void> delete(@CurrentUserEmail String email,
                                    @PathVariable(name = "challenge-id") Long challengeId) {
        challengeService.delete(email, challengeId);
        return new RspTemplate<>(HttpStatus.OK, "챌린지 삭제 성공");
    }
}
