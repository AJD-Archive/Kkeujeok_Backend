package shop.kkeujeok.kkeujeokbackend.challenge.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.requestFields;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.responseFields;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.TokenReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.Type;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSearchReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeInfoResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeListResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Challenge;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Cycle;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.CycleDetail;
import shop.kkeujeok.kkeujeokbackend.common.annotation.ControllerTest;
import shop.kkeujeok.kkeujeokbackend.global.annotationresolver.CurrentUserEmailArgumentResolver;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.global.error.ControllerAdvice;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.Role;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;

@ExtendWith(RestDocumentationExtension.class)
class ChallengeControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer valid-token";

    private Member member;
    private Challenge challenge;
    private ChallengeSaveReqDto challengeSaveReqDto;
    private ChallengeSaveReqDto challengeUpdateReqDto;
    private ChallengeSearchReqDto challengeSearchReqDto;

    @InjectMocks
    ChallengeController challengeController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        member = Member.builder().status(Status.ACTIVE).email("kkeujeok@gmail.com").name("김동균").picture("기본 프로필")
                .socialType(SocialType.GOOGLE).role(Role.ROLE_USER).firstLogin(true).nickname("동동").build();

        challengeSaveReqDto = new ChallengeSaveReqDto("1일 1커밋", "1일 1커밋하기", Cycle.WEEKLY,
                List.of(CycleDetail.MON, CycleDetail.TUE), LocalDate.now(), LocalDate.now().plusDays(30), "대표 이미지");

        challenge = Challenge.builder().title(challengeSaveReqDto.title()).contents(challengeSaveReqDto.title())
                .cycleDetails(challengeSaveReqDto.cycleDetails()).startDate(challengeSaveReqDto.startDate())
                .endDate(challengeSaveReqDto.endDate()).representImage(challengeSaveReqDto.representImage())
                .member(member).build();

        challengeUpdateReqDto = new ChallengeSaveReqDto("업데이트 제목", "업데이트 내용", Cycle.WEEKLY, List.of(CycleDetail.MON),
                LocalDate.now(), LocalDate.now().plusDays(30), "업데이트 이미지");

        challengeSearchReqDto = new ChallengeSearchReqDto("1일");

        challengeController = new ChallengeController(challengeService);

        mockMvc = MockMvcBuilders.standaloneSetup(challengeController)
                .apply(documentationConfiguration(restDocumentation))
                .setCustomArgumentResolvers(new CurrentUserEmailArgumentResolver(tokenProvider))
                .setControllerAdvice(new ControllerAdvice()).build();

        when(tokenProvider.getUserEmailFromToken(any(TokenReqDto.class))).thenReturn("kkeujeok@gmail.com");
    }

    @Test
    @DisplayName("챌린지 생성 성공 시 상태코드 201 반환")
    void 챌린지_생성_성공_시_상태코드_201_반환() throws Exception {
        // given
        ChallengeInfoResDto response = ChallengeInfoResDto.from(challenge);
        given(challengeService.save(anyString(), any(ChallengeSaveReqDto.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/challenges").header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(challengeSaveReqDto))).andDo(print())
                .andDo(document("challenge/save", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 토큰")),
                        requestFields(fieldWithPath("title").description("챌린지 제목"),
                                fieldWithPath("contents").description("챌린지 내용"),
                                fieldWithPath("cycle").description("챌린지 주기"),
                                fieldWithPath("cycleDetails").description("주기 상세정보"),
                                fieldWithPath("startDate").description("시작 날짜"),
                                fieldWithPath("endDate").description("종료 날짜"),
                                fieldWithPath("representImage").description("대표 사진")),
                        responseFields(fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.title").description("챌린지 제목"),
                                fieldWithPath("data.contents").description("챌린지 내용"),
                                fieldWithPath("data.cycle").description("챌린지 주기"),
                                fieldWithPath("data.cycleDetails").description("주기 상세정보"),
                                fieldWithPath("data.startDate").description("시작 날짜"),
                                fieldWithPath("data.endDate").description("종료 날짜"),
                                fieldWithPath("data.representImage").description("대표 사진"),
                                fieldWithPath("data.authorName").description("챌린지 작성자 이름"),
                                fieldWithPath("data.authorProfileImage").description("챌린지 작성자 프로필 이미지"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("블록 수정에 성공하면 상태코드 200 반환")
    void 블록_수정에_성공하면_상태코드_200_반환() throws Exception {
        // given
        challenge.update(challengeUpdateReqDto.title(), challengeUpdateReqDto.contents(),
                challengeUpdateReqDto.cycleDetails(), challengeUpdateReqDto.startDate(),
                challengeUpdateReqDto.endDate(), challengeUpdateReqDto.representImage());
        ChallengeInfoResDto response = ChallengeInfoResDto.from(challenge);
        given(challengeService.update(anyString(), anyLong(), any(ChallengeSaveReqDto.class))).willReturn(response);

        // when & then
        mockMvc.perform(
                        patch("/api/challenges/{challengeId}", 1L).header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(challengeSaveReqDto))).andDo(print())
                .andDo(document("challenge/update", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 토큰")),
                        pathParameters(parameterWithName("challengeId").description("챌린지 ID")),
                        requestFields(fieldWithPath("title").description("챌린지 제목"),
                                fieldWithPath("contents").description("챌린지 내용"),
                                fieldWithPath("cycle").description("챌린지 주기"),
                                fieldWithPath("cycleDetails").description("주기 상세정보"),
                                fieldWithPath("startDate").description("시작 날짜"),
                                fieldWithPath("endDate").description("종료 날짜"),
                                fieldWithPath("representImage").description("대표 사진")),
                        responseFields(fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.title").description("챌린지 제목"),
                                fieldWithPath("data.contents").description("챌린지 내용"),
                                fieldWithPath("data.cycle").description("챌린지 주기"),
                                fieldWithPath("data.cycleDetails").description("주기 상세정보"),
                                fieldWithPath("data.startDate").description("시작 날짜"),
                                fieldWithPath("data.endDate").description("종료 날짜"),
                                fieldWithPath("data.representImage").description("대표 사진"),
                                fieldWithPath("data.authorName").description("챌린지 작성자 이름"),
                                fieldWithPath("data.authorProfileImage").description("챌린지 작성자 프로필 이미지"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("챌린지 전체 조회에 성공하면 상태코드 200 반환")
    void 챌린지_전체_조회에_성공하면_상태코드_200_반환() throws Exception {
        // given
        ChallengeInfoResDto challengeInfoResDto = ChallengeInfoResDto.from(challenge);
        Page<Challenge> challengePage = new PageImpl<>(List.of(challenge), PageRequest.of(0, 10), 1);
        ChallengeListResDto response = ChallengeListResDto.of(List.of(challengeInfoResDto),
                PageInfoResDto.from(challengePage));
        given(challengeService.findAllChallenges(any(PageRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(
                        get("/api/challenges").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andDo(document("challenge/findAll", preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()), responseFields(fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.challengeInfoResDto[].title").description("챌린지 제목"),
                                fieldWithPath("data.challengeInfoResDto[].contents").description("챌린지 내용"),
                                fieldWithPath("data.challengeInfoResDto[].cycle").description("챌린지 주기"),
                                fieldWithPath("data.challengeInfoResDto[].cycleDetails[]").description("주기 상세정보"),
                                fieldWithPath("data.challengeInfoResDto[].startDate").description("시작 날짜"),
                                fieldWithPath("data.challengeInfoResDto[].endDate").description("종료 날짜"),
                                fieldWithPath("data.challengeInfoResDto[].representImage").description("대표 사진"),
                                fieldWithPath("data.challengeInfoResDto[].authorName").description("챌린지 작성자 이름"),
                                fieldWithPath("data.challengeInfoResDto[].authorProfileImage").description("챌린지 작성자 프로필 이미지"),
                                fieldWithPath("data.pageInfoResDto.currentPage").description("현재 페이지"),
                                fieldWithPath("data.pageInfoResDto.totalPages").description("전체 페이지"),
                                fieldWithPath("data.pageInfoResDto.totalItems").description("전체 아이템")

                        ))).andExpect(status().isOk());
    }

    @Test
    @DisplayName("검색에 성공하면 상태코드 200 반환")
    void 검색에_성공하면_상태코드_200_반환() throws Exception {
        // given
        ChallengeInfoResDto challengeInfoResDto = ChallengeInfoResDto.from(challenge);
        Page<Challenge> challengePage = new PageImpl<>(List.of(challenge), PageRequest.of(0, 10), 1);
        ChallengeListResDto response = ChallengeListResDto.of(List.of(challengeInfoResDto),
                PageInfoResDto.from(challengePage));
        given(challengeService.findChallengesByKeyWord(any(ChallengeSearchReqDto.class),
                any(PageRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/challenges/search?keyword=%s", challengeSearchReqDto.keyWord()).accept(
                        MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andDo(document("challenge/search", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        responseFields(fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.challengeInfoResDto[].title").description("챌린지 제목"),
                                fieldWithPath("data.challengeInfoResDto[].contents").description("챌린지 내용"),
                                fieldWithPath("data.challengeInfoResDto[].cycle").description("챌린지 주기"),
                                fieldWithPath("data.challengeInfoResDto[].cycleDetails[]").description("주기 상세정보"),
                                fieldWithPath("data.challengeInfoResDto[].startDate").description("시작 날짜"),
                                fieldWithPath("data.challengeInfoResDto[].endDate").description("종료 날짜"),
                                fieldWithPath("data.challengeInfoResDto[].representImage").description("대표 사진"),
                                fieldWithPath("data.challengeInfoResDto[].authorName").description("챌린지 작성자 이름"),
                                fieldWithPath("data.challengeInfoResDto[].authorProfileImage").description(
                                        "챌린지 작성자 프로필 이미지"),
                                fieldWithPath("data.pageInfoResDto.currentPage").description("현재 페이지"),
                                fieldWithPath("data.pageInfoResDto.totalPages").description("전체 페이지"),
                                fieldWithPath("data.pageInfoResDto.totalItems").description("전체 아이템")

                        ))).andExpect(status().isOk());

    }

    @Test
    @DisplayName("챌린지 상세 정보 조회에 성공하면 상태코드 200 반환")
    void 챌린지_상세_조회에_성공하면_상태코드_200_반환() throws Exception {
        // given
        ChallengeInfoResDto response = ChallengeInfoResDto.from(challenge);
        given(challengeService.findById(anyLong())).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/challenges/{challengeId}", 1L).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andDo(document("challenge/findById", preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("challengeId").description("챌린지 ID")),
                        responseFields(fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.title").description("챌린지 제목"),
                                fieldWithPath("data.contents").description("챌린지 내용"),
                                fieldWithPath("data.cycle").description("챌린지 주기"),
                                fieldWithPath("data.cycleDetails").description("주기 상세정보"),
                                fieldWithPath("data.startDate").description("시작 날짜"),
                                fieldWithPath("data.endDate").description("종료 날짜"),
                                fieldWithPath("data.representImage").description("대표 사진"),
                                fieldWithPath("data.authorName").description("챌린지 작성자 이름"),
                                fieldWithPath("data.authorProfileImage").description("챌린지 작성자 프로필 이미지"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("챌린지 삭제에 성공하면 상태코드 200 반환")
    void 챌린지_삭제에_성공하면_상태코드_200_반환() throws Exception {
        // given
        willDoNothing().given(challengeService).delete(anyString(), anyLong());

        // when & then
        mockMvc.perform(delete("/api/challenges/{challengeId}", 1L).header(AUTHORIZATION_HEADER_NAME,
                                AUTHORIZATION_HEADER_VALUE).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(challengeSaveReqDto))).andDo(print())
                .andDo(document("challenge/delete", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 토큰")),
                        pathParameters(parameterWithName("challengeId").description("챌린지 ID"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("챌린지를 개인 대시보드에 추가 성공 시 상태코드 200 반환")
    void 챌린지를_개인_대시보드에_추가_성공_시_상태코드_200_반환() throws Exception {
        // given
        BlockInfoResDto blockInfoResDto = new BlockInfoResDto(1L, "1일 1커밋", "1일 1커밋하기", Progress.NOT_STARTED,
                Type.CHALLENGE, "2024.09.31 23:59", "2024.09.31 23:59", "동동", 0);

        given(challengeService.addChallengeToPersonalDashboard(anyString(), anyLong(), anyLong())).willReturn(
                blockInfoResDto);

        // when & then
        mockMvc.perform(post("/api/challenges/{challengeId}/{dashboardId}", 1L, 1L).header(AUTHORIZATION_HEADER_NAME,
                                AUTHORIZATION_HEADER_VALUE).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blockInfoResDto)))

                .andDo(print())
                .andDo(document("challenge/addChallengeToPersonalDashboard", preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 토큰")),
                        pathParameters(parameterWithName("challengeId").description("챌린지 ID"),
                                parameterWithName("dashboardId").description("대시보드 ID")),
                        responseFields(fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.blockId").description("블록 ID"),
                                fieldWithPath("data.title").description("블록 제목"),
                                fieldWithPath("data.contents").description("블록 내용"),
                                fieldWithPath("data.progress").description("블록 진행도"),
                                fieldWithPath("data.type").description("블록 타입"),
                                fieldWithPath("data.startDate").description("블록 시작기한"),
                                fieldWithPath("data.deadLine").description("블록 마감기한"),
                                fieldWithPath("data.nickname").description("블록 작성자"),
                                fieldWithPath("data.dDay").description("블록 디데이")))).andExpect(status().isOk());
    }
}