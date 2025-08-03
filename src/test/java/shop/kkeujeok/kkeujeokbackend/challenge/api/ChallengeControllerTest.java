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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.TokenReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.domain.Type;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.reqeust.ChallengeSearchReqDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeCompletedMemberInfoResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeInfoResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.api.dto.response.ChallengeListResDto;
import shop.kkeujeok.kkeujeokbackend.challenge.domain.Category;
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
        member = Member.builder()
                .status(Status.ACTIVE)
                .email("kkeujeok@gmail.com")
                .name("김동균")
                .picture("기본 프로필")
                .socialType(SocialType.GOOGLE)
                .role(Role.ROLE_USER)
                .firstLogin(true)
                .nickname("동동")
                .build();

        challengeSaveReqDto = new ChallengeSaveReqDto(
                "챌린지 제목",
                "챌린지 내용",
                Category.CREATIVITY_AND_ARTS,
                Cycle.WEEKLY,
                List.of(CycleDetail.MON, CycleDetail.TUE),
                LocalDate.now(),
                "블록 이름");

        challenge = Challenge.builder()
                .title(challengeSaveReqDto.title())
                .contents(challengeSaveReqDto.title())
                .category(challengeSaveReqDto.category())
                .cycle(challengeSaveReqDto.cycle())
                .cycleDetails(challengeSaveReqDto.cycleDetails())
                .endDate(challengeSaveReqDto.endDate())
                .member(member)
                .blockName(challengeSaveReqDto.blockName())
                .representImage("대표 이미지")
                .build();

        ReflectionTestUtils.setField(challenge, "id", 1L);
        ReflectionTestUtils.setField(challenge, "startDate", LocalDate.now());

        challengeUpdateReqDto = new ChallengeSaveReqDto(
                "업데이트 제목",
                "업데이트 내용",
                Category.CREATIVITY_AND_ARTS,
                Cycle.WEEKLY,
                List.of(CycleDetail.MON),
                LocalDate.now(),
                "1일 1커밋");

        challengeSearchReqDto = new ChallengeSearchReqDto("챌린지", "CREATIVITY_AND_ARTS");

        challengeController = new ChallengeController(challengeService);

        mockMvc = MockMvcBuilders.standaloneSetup(challengeController)
                .apply(documentationConfiguration(restDocumentation))
                .setCustomArgumentResolvers(new CurrentUserEmailArgumentResolver(tokenProvider))
                .setControllerAdvice(new ControllerAdvice()).build();

        when(tokenProvider.getUserEmailFromToken(any(TokenReqDto.class)))
                .thenReturn("kkeujeok@gmail.com");
    }


    @Test
    @DisplayName("챌린지 생성 성공 시 상태코드 201 반환")
    void 챌린지_생성_성공_시_상태코드_201_반환() throws Exception {
        // given
        ChallengeInfoResDto response = ChallengeInfoResDto.from(challenge);

        given(challengeService.save(anyString(), any(ChallengeSaveReqDto.class), any(MultipartFile.class)))
                .willReturn(response);

        MockMultipartFile file = new MockMultipartFile("representImage", "test-image.jpg", "image/jpeg",
                "test image content".getBytes());

        String challengeDtoJson = objectMapper.writeValueAsString(challengeSaveReqDto);

        MockMultipartFile dtoPart = new MockMultipartFile("challengeSaveReqDto", "challengeSaveReqDto.json",
                "application/json", challengeDtoJson.getBytes());

        // when & then
        mockMvc.perform(
                        multipart("/api/challenges")
                                .file(file)
                                .file(dtoPart)
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andDo(document("challenge/save",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 토큰")),
                                requestParts(
                                        partWithName("challengeSaveReqDto").description("챌린지 DTO"),
                                        partWithName("representImage").description("대표 이미지")
                                ),
                                responseFields(
                                        fieldWithPath("statusCode").description("상태 코드"),
                                        fieldWithPath("message").description("응답 메시지"),
                                        fieldWithPath("data.challengeId").description("챌린지 id"),
                                        fieldWithPath("data.authorId").description("챌린지 작성자 id"),
                                        fieldWithPath("data.title").description("챌린지 제목"),
                                        fieldWithPath("data.contents").description("챌린지 내용"),
                                        fieldWithPath("data.category").description("챌린지 카테고리"),
                                        fieldWithPath("data.cycle").description("챌린지 주기"),
                                        fieldWithPath("data.cycleDetails").description("주기 상세정보"),
                                        fieldWithPath("data.startDate").description("시작 날짜"),
                                        fieldWithPath("data.endDate").description("종료 날짜"),
                                        fieldWithPath("data.representImage").description("대표 사진"),
                                        fieldWithPath("data.authorName").description("챌린지 작성자 이름"),
                                        fieldWithPath("data.authorProfileImage").description("챌린지 작성자 프로필 이미지"),
                                        fieldWithPath("data.blockName").description("블록 이름"),
                                        fieldWithPath("data.participantCount").description("참여자 수"),
                                        fieldWithPath("data.isParticipant").description("참여 여부"),
                                        fieldWithPath("data.isAuthor").description("작성자 여부"),
                                        fieldWithPath("data.completedMembers[]").description("완료한 회원 목록"),
                                        fieldWithPath("data.createdAt").description("챌린지 생성일")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("챌린지 수정에 성공하면 상태코드 200 반환")
    void 챌린지_수정에_성공하면_상태코드_200_반환() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("representImage", "test-image.jpg", "image/jpeg",
                "test image content".getBytes());

        String challengeDtoJson = objectMapper.writeValueAsString(challengeUpdateReqDto);

        MockMultipartFile dtoPart = new MockMultipartFile("challengeSaveReqDto", "challengeSaveReqDto.json",
                "application/json", challengeDtoJson.getBytes());

        ChallengeInfoResDto response = ChallengeInfoResDto.from(challenge);
        given(challengeService.update(anyString(), anyLong(), any(ChallengeSaveReqDto.class), any(MultipartFile.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(multipart("/api/challenges/{challengeId}", 1L)
                        .file(file) // 이미지 파일 포함
                        .file(dtoPart) // DTO 포함
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                )
                .andDo(print())
                .andDo(document("challenge/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 토큰")),
                        pathParameters(parameterWithName("challengeId").description("챌린지 ID")),
                        requestParts(
                                partWithName("challengeSaveReqDto").description("챌린지 DTO"),
                                partWithName("representImage").description("대표 이미지")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.challengeId").description("챌린지 id"),
                                fieldWithPath("data.authorId").description("챌린지 작성자 id"),
                                fieldWithPath("data.title").description("챌린지 제목"),
                                fieldWithPath("data.contents").description("챌린지 내용"),
                                fieldWithPath("data.category").description("챌린지 카테고리"),
                                fieldWithPath("data.cycle").description("챌린지 주기"),
                                fieldWithPath("data.cycleDetails").description("주기 상세정보"),
                                fieldWithPath("data.startDate").description("시작 날짜"),
                                fieldWithPath("data.endDate").description("종료 날짜"),
                                fieldWithPath("data.representImage").description("대표 사진"),
                                fieldWithPath("data.authorName").description("챌린지 작성자 이름"),
                                fieldWithPath("data.authorProfileImage").description("챌린지 작성자 프로필 이미지"),
                                fieldWithPath("data.blockName").description("블록 이름"),
                                fieldWithPath("data.participantCount").description("참여자 수"),
                                fieldWithPath("data.isParticipant").description("참여 여부"),
                                fieldWithPath("data.isAuthor").description("작성자 여부"),
                                fieldWithPath("data.completedMembers[]").description("완료한 회원 목록"),
                                fieldWithPath("data.createdAt").description("챌린지 생성일")
                        ))
                )
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("챌린지 전체 조회에 성공하면 상태코드 200 반환")
    void 챌린지_전체_조회에_성공하면_상태코드_200_반환() throws Exception {
        // given
        ChallengeInfoResDto challengeInfoResDto = ChallengeInfoResDto.from(challenge);
        Page<Challenge> challengePage = new PageImpl<>(List.of(challenge),
                PageRequest.of(0, 10), 1);
        ChallengeListResDto response = ChallengeListResDto.of(List.of(challengeInfoResDto),
                PageInfoResDto.from(challengePage));
        given(challengeService.findAllChallenges(any(PageRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(
                        get("/api/challenges")
                                .param("page", "0")
                                .param("size", "10")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(
                        document("challenge/findAll",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(
                                        parameterWithName("page").description("페이지 번호"),
                                        parameterWithName("size").description("페이지 크기")
                                ),
                                responseFields(
                                        fieldWithPath("statusCode").description("상태 코드"),
                                        fieldWithPath("message").description("응답 메시지"),

                                        fieldWithPath("data.challengeInfoResDto[].challengeId").description("챌린지 id"),
                                        fieldWithPath("data.challengeInfoResDto[].authorId").description("챌린지 작성자"),
                                        fieldWithPath("data.challengeInfoResDto[].title").description("챌린지 제목"),
                                        fieldWithPath("data.challengeInfoResDto[].contents").description("챌린지 내용"),
                                        fieldWithPath("data.challengeInfoResDto[].category").description("챌린지 카테고리"),
                                        fieldWithPath("data.challengeInfoResDto[].cycle").description("챌린지 주기"),
                                        fieldWithPath("data.challengeInfoResDto[].cycleDetails[]").description(
                                                "주기 상세정보"),
                                        fieldWithPath("data.challengeInfoResDto[].startDate").description("시작 날짜"),
                                        fieldWithPath("data.challengeInfoResDto[].endDate").description("종료 날짜"),
                                        fieldWithPath("data.challengeInfoResDto[].representImage").description("대표 사진"),
                                        fieldWithPath("data.challengeInfoResDto[].authorName").description(
                                                "챌린지 작성자 이름"),
                                        fieldWithPath("data.challengeInfoResDto[].authorProfileImage").description(
                                                "챌린지 작성자 프로필 이미지"),
                                        fieldWithPath("data.challengeInfoResDto[].participantCount").description(
                                                "참여자 수"),
                                        fieldWithPath("data.challengeInfoResDto[].isParticipant").description("참여 여부"),
                                        fieldWithPath("data.challengeInfoResDto[].isAuthor").description("작성자 여부"),
                                        fieldWithPath("data.challengeInfoResDto[].blockName").description("블록 이름"),
                                        fieldWithPath("data.challengeInfoResDto[].completedMembers[]").description(
                                                "완료한 회원 목록"),
                                        fieldWithPath("data.challengeInfoResDto[].createdAt").description("챌린지 생성일"),

                                        fieldWithPath("data.pageInfoResDto.currentPage").description("현재 페이지"),
                                        fieldWithPath("data.pageInfoResDto.totalPages").description("전체 페이지"),
                                        fieldWithPath("data.pageInfoResDto.totalItems").description("전체 아이템")
                                )
                        )
                ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("카테고리 별 검색에 성공하면 상태코드 200 반환")
    void 카테고리_별_검색에_성공하면_상태코드_200_반환() throws Exception {
        // given
        ChallengeInfoResDto challengeInfoResDto = ChallengeInfoResDto.from(challenge);
        Page<Challenge> challengePage = new PageImpl<>(List.of(challenge),
                PageRequest.of(0, 10), 1);
        ChallengeListResDto response = ChallengeListResDto.of(List.of(challengeInfoResDto),
                PageInfoResDto.from(challengePage));
        given(challengeService.findChallengesByCategoryAndKeyword(any(ChallengeSearchReqDto.class),
                any(PageRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(
                        get("/api/challenges/search")
                                .param("category", challengeSearchReqDto.category())
                                .param("keyword", challengeSearchReqDto.keyWord())
                                .param("page", "0")
                                .param("size", "10")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(
                        document("challenge/search",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(
                                        parameterWithName("category").description("카테고리"),
                                        parameterWithName("keyword").description("검색 키워드"),
                                        parameterWithName("page").description("페이지 번호"),
                                        parameterWithName("size").description("페이지 크기")
                                ),
                                responseFields(
                                        fieldWithPath("statusCode").description("상태 코드"),
                                        fieldWithPath("message").description("응답 메시지"),

                                        fieldWithPath("data.challengeInfoResDto[].challengeId").description("챌린지 id"),
                                        fieldWithPath("data.challengeInfoResDto[].authorId").description("챌린지 작성자"),
                                        fieldWithPath("data.challengeInfoResDto[].title").description("챌린지 제목"),
                                        fieldWithPath("data.challengeInfoResDto[].contents").description("챌린지 내용"),
                                        fieldWithPath("data.challengeInfoResDto[].category").description("챌린지 카테고리"),
                                        fieldWithPath("data.challengeInfoResDto[].cycle").description("챌린지 주기"),
                                        fieldWithPath("data.challengeInfoResDto[].cycleDetails[]").description(
                                                "주기 상세정보"),
                                        fieldWithPath("data.challengeInfoResDto[].startDate").description("시작 날짜"),
                                        fieldWithPath("data.challengeInfoResDto[].endDate").description("종료 날짜"),
                                        fieldWithPath("data.challengeInfoResDto[].representImage").description("대표 사진"),
                                        fieldWithPath("data.challengeInfoResDto[].authorName").description(
                                                "챌린지 작성자 이름"),
                                        fieldWithPath("data.challengeInfoResDto[].authorProfileImage").description(
                                                "챌린지 작성자 프로필 이미지"),
                                        fieldWithPath("data.challengeInfoResDto[].participantCount").description(
                                                "참여자 수"),
                                        fieldWithPath("data.challengeInfoResDto[].isParticipant").description("참여 여부"),
                                        fieldWithPath("data.challengeInfoResDto[].isAuthor").description("작성자 여부"),
                                        fieldWithPath("data.challengeInfoResDto[].blockName").description("블록 이름"),
                                        fieldWithPath("data.challengeInfoResDto[].completedMembers[]").description(
                                                "완료한 회원 목록"),
                                        fieldWithPath("data.challengeInfoResDto[].createdAt").description("챌린지 생성일"),

                                        fieldWithPath("data.pageInfoResDto.currentPage").description("현재 페이지"),
                                        fieldWithPath("data.pageInfoResDto.totalPages").description("전체 페이지"),
                                        fieldWithPath("data.pageInfoResDto.totalItems").description("전체 아이템")
                                )
                        )
                )
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("챌린지 상세 정보 조회에 성공하면 상태코드 200 반환")
    void 챌린지_상세_조회에_성공하면_상태코드_200_반환() throws Exception {
        // given
        ChallengeCompletedMemberInfoResDto challengeCompletedMemberInfoResDto = ChallengeCompletedMemberInfoResDto.from(
                member);
        ChallengeInfoResDto response = ChallengeInfoResDto.from(challenge);
        given(challengeService.findById(anyString(), anyLong()))
                .willReturn(response);

        // when & then
        mockMvc.perform(
                        get("/api/challenges/{challengeId}", 1L)
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("challenge/findById",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 토큰")),
                        pathParameters(parameterWithName("challengeId").description("챌린지 ID")),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.challengeId").description("챌린지 id"),
                                fieldWithPath("data.authorId").description("챌린지 작성자 id"),
                                fieldWithPath("data.title").description("챌린지 제목"),
                                fieldWithPath("data.contents").description("챌린지 내용"),
                                fieldWithPath("data.category").description("챌린지 카테고리"),
                                fieldWithPath("data.cycle").description("챌린지 주기"),
                                fieldWithPath("data.cycleDetails").description("주기 상세정보"),
                                fieldWithPath("data.startDate").description("시작 날짜"),
                                fieldWithPath("data.endDate").description("종료 날짜"),
                                fieldWithPath("data.representImage").description("대표 사진"),
                                fieldWithPath("data.authorName").description("챌린지 작성자 이름"),
                                fieldWithPath("data.authorProfileImage").description("챌린지 작성자 프로필 이미지"),
                                fieldWithPath("data.blockName").description("블록 이름"),
                                fieldWithPath("data.participantCount").description("참여자 수"),
                                fieldWithPath("data.isParticipant").description("참여 여부"),
                                fieldWithPath("data.isAuthor").description("작성자 여부"),
                                fieldWithPath("data.completedMembers[]").description("완료한 회원 목록"),
                                fieldWithPath("data.createdAt").description("챌린지 생성일")
                        ))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("챌린지 삭제에 성공하면 상태코드 200 반환")
    void 챌린지_삭제에_성공하면_상태코드_200_반환() throws Exception {
        // given
        willDoNothing().given(challengeService).delete(anyString(), anyLong());

        // when & then
        mockMvc.perform(delete("/api/challenges/{challengeId}", 1L)
                        .header(AUTHORIZATION_HEADER_NAME,
                                AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(challengeSaveReqDto)))
                .andDo(print())
                .andDo(document("challenge/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 토큰")),
                        pathParameters(parameterWithName("challengeId").description("챌린지 ID")
                        ))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("챌린지를 개인 대시보드에 추가 성공 시 상태코드 200 반환")
    void 챌린지를_개인_대시보드에_추가_성공_시_상태코드_200_반환() throws Exception {
        // given
        BlockInfoResDto blockInfoResDto = new BlockInfoResDto(1L,
                "1일 1커밋",
                "1일 1커밋하기",
                Progress.NOT_STARTED,
                Type.CHALLENGE,
                "PersonalDashboard",
                "2024.09.31 23:59",
                "2024.09.31 23:59",
                "동동",
                "picture",
                "0");

        willDoNothing().given(challengeService).addChallengeToPersonalDashboard(anyString(), anyLong(), anyLong());

        // when & then
        mockMvc.perform(post("/api/challenges/{challengeId}/{dashboardId}", 1L, 1L)
                        .header(AUTHORIZATION_HEADER_NAME,
                                AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blockInfoResDto)))
                .andDo(print())
                .andDo(document("challenge/addChallengeToPersonalDashboard",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 토큰")),
                                pathParameters(parameterWithName("challengeId").description("챌린지 ID"),
                                        parameterWithName("dashboardId").description("대시보드 ID"))
                        )
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("챌린지 탈퇴에 성공하면 상태코드 200 반환")
    void 챌린지_탈퇴에_성공하면_상태코드_200_반환() throws Exception {
        // given
        willDoNothing().given(challengeService).withdrawFromChallenge(anyString(), anyLong());

        // when & then
        mockMvc.perform(delete("/api/challenges/{challengeId}/withdraw", 1L)
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("challenge/withdraw",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 토큰")),
                        pathParameters(parameterWithName("challengeId").description("챌린지 ID"))
                ))
                .andExpect(status().isOk());
    }

}