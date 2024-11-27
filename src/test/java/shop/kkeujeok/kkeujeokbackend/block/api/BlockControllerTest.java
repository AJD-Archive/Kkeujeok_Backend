package shop.kkeujeok.kkeujeokbackend.block.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.requestFields;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.responseFields;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.TokenReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockSequenceUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockListResDto;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.exception.InvalidProgressException;
import shop.kkeujeok.kkeujeokbackend.common.annotation.ControllerTest;
import shop.kkeujeok.kkeujeokbackend.dashboard.domain.Dashboard;
import shop.kkeujeok.kkeujeokbackend.dashboard.personal.domain.PersonalDashboard;
import shop.kkeujeok.kkeujeokbackend.global.annotationresolver.CurrentUserEmailArgumentResolver;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.global.error.ControllerAdvice;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;

class BlockControllerTest extends ControllerTest {

    private Member member;
    private Block block;
    private BlockSaveReqDto blockSaveReqDto;
    private BlockUpdateReqDto blockUpdateReqDto;
    private BlockSequenceUpdateReqDto blockSequenceUpdateReqDto;

    @InjectMocks
    BlockController blockController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        member = Member.builder()
                .email("email")
                .name("name")
                .nickname("nickname")
                .socialType(SocialType.GOOGLE)
                .introduction("introduction")
                .picture("picture")
                .build();

        Dashboard dashboard = PersonalDashboard.builder()
                .member(member)
                .title("title")
                .description("description")
                .dType("PersonalDashboard")
                .isPublic(false)
                .category("category")
                .build();

        blockSaveReqDto = new BlockSaveReqDto(1L, "Title", "Contents", Progress.NOT_STARTED, "2024.07.03 13:23",
                "2024.08.03 13:23");
        blockUpdateReqDto = new BlockUpdateReqDto("UpdateTitle", "UpdateContents", "2024.07.03 13:23",
                "2024.07.28 16:40");
        block = blockSaveReqDto.toEntity(member, dashboard, 0);

        ReflectionTestUtils.setField(block, "id", 1L);

        blockSequenceUpdateReqDto = new BlockSequenceUpdateReqDto(
                dashboard.getId(),
                List.of(2L, 3L),
                List.of(3L, 1L),
                List.of(1L, 2L)
        );

        blockController = new BlockController(blockService);

        mockMvc = MockMvcBuilders.standaloneSetup(blockController)
                .apply(documentationConfiguration(restDocumentation))
                .setCustomArgumentResolvers(new CurrentUserEmailArgumentResolver(tokenProvider))
                .setControllerAdvice(new ControllerAdvice())
                .build();

        when(tokenProvider.getUserEmailFromToken(any(TokenReqDto.class))).thenReturn("email");
    }

    @DisplayName("POST 블록 저장 컨트롤러 로직 확인")
    @Test
    void 블록_저장() throws Exception {
        // given
        BlockInfoResDto response = BlockInfoResDto.from(block);
        given(blockService.save(anyString(), any(BlockSaveReqDto.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/blocks/")
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blockSaveReqDto)))
                .andDo(print())
                .andDo(document("block/save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("dashboardId").description("대시보드 아이디"),
                                fieldWithPath("title").description("블록 제목"),
                                fieldWithPath("contents").description("블록 내용"),
                                fieldWithPath("progress").description("블록 진행 상태"),
                                fieldWithPath("startDate").description("블록 시작 시간"),
                                fieldWithPath("deadLine").description("블록 마감 시간")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.blockId").description("블록 아이디"),
                                fieldWithPath("data.title").description("블록 제목"),
                                fieldWithPath("data.contents").description("블록 내용"),
                                fieldWithPath("data.progress").description("블록 진행 상태"),
                                fieldWithPath("data.type").description(
                                        "블록 타입(일반(General) 블록인지 챌린지(Challenge) 블록인지 구별)"),
                                fieldWithPath("data.dType").description("개인 대시보드, 팀 대시보드를 구별"),
                                fieldWithPath("data.startDate").description("블록 시작 시간"),
                                fieldWithPath("data.deadLine").description("블록 마감 시간"),
                                fieldWithPath("data.nickname").description("회원 닉네임"),
                                fieldWithPath("data.picture").description("회원 사진"),
                                fieldWithPath("data.dDay").description("마감 기한")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("PATCH 블록 수정 컨트롤러 로직 확인")
    @Test
    void 블록_수정() throws Exception {
        // given
        block.update(blockUpdateReqDto.title(), blockUpdateReqDto.contents(), blockUpdateReqDto.startDate(),
                blockUpdateReqDto.deadLine());
        BlockInfoResDto response = BlockInfoResDto.from(block);
        given(blockService.update(anyString(), anyLong(), any(BlockUpdateReqDto.class))).willReturn(response);

        // when & then
        mockMvc.perform(patch("/api/blocks/{blockId}", 1L)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blockUpdateReqDto)))
                .andDo(print())
                .andDo(document("block/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("blockId").description("블록 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("블록 제목"),
                                fieldWithPath("contents").description("블록 내용"),
                                fieldWithPath("startDate").description("블록 시작 시간"),
                                fieldWithPath("deadLine").description("블록 마감 시간")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.blockId").description("블록 아이디"),
                                fieldWithPath("data.title").description("블록 제목"),
                                fieldWithPath("data.contents").description("블록 내용"),
                                fieldWithPath("data.progress").description("블록 진행 상태"),
                                fieldWithPath("data.type").description(
                                        "블록 타입(일반(General) 블록인지 챌린지(Challenge) 블록인지 구별)"),
                                fieldWithPath("data.dType").description("개인 대시보드, 팀 대시보드를 구별"),
                                fieldWithPath("data.startDate").description("블록 시작 시간"),
                                fieldWithPath("data.deadLine").description("블록 마감 시간"),
                                fieldWithPath("data.nickname").description("회원 닉네임"),
                                fieldWithPath("data.picture").description("회원 사진"),
                                fieldWithPath("data.dDay").description("마감 기한")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("PATCH 블록 상태 수정 컨트롤러 로직 확인")
    @Test
    void 블록_상태_수정() throws Exception {
        // given
        Long blockId = 1L;
        String progressString = "IN_PROGRESS";
        BlockInfoResDto response = BlockInfoResDto.from(block);

        given(blockService.progressUpdate(anyString(), anyLong(), anyString())).willReturn(response);

        // when & then
        mockMvc.perform(patch(String.format("/api/blocks/{blockId}/progress?progress=%s", progressString), blockId)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("block/progress/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("blockId").description("블록 ID")
                        ),
                        queryParameters(
                                parameterWithName("progress")
                                        .description("블록 상태 문자열(NOT_STARTED, IN_PROGRESS, COMPLETED)")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.blockId").description("블록 아이디"),
                                fieldWithPath("data.title").description("블록 제목"),
                                fieldWithPath("data.contents").description("블록 내용"),
                                fieldWithPath("data.startDate").description("블록 시작 시간"),
                                fieldWithPath("data.deadLine").description("블록 마감 시간"),
                                fieldWithPath("data.type").description(
                                        "블록 타입(일반(General) 블록인지 챌린지(Challenge) 블록인지 구별)"),
                                fieldWithPath("data.dType").description("개인 대시보드, 팀 대시보드를 구별"),
                                fieldWithPath("data.progress").description("블록 진행 상태"),
                                fieldWithPath("data.nickname").description("회원 닉네임"),
                                fieldWithPath("data.picture").description("회원 사진"),
                                fieldWithPath("data.dDay").description("마감 기한")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("Patch 블록 상태 수정 실패 컨트롤러 로직 확인(400 Bad Request가 발생한다)")
    @Test
    void 블록_상태_수정_실패() throws Exception {
        // given
        Long blockId = 1L;
        String progressString = "STATUS_PROGRESS";

        given(blockService.progressUpdate(anyString(), anyLong(), anyString())).willThrow(
                new InvalidProgressException());

        // when & then
        mockMvc.perform(patch(String.format("/api/blocks/{blockId}/progress?progress=%s", progressString), blockId)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("block/progress/update/failure",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("blockId").description("블록 ID")
                        ),
                        queryParameters(
                                parameterWithName("progress")
                                        .description("블록 상태 문자열(NOT_STARTED, IN_PROGRESS, COMPLETED)")
                        )
                ))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Delete 블록을 논리적으로 삭제하고, 복구합니다.")
    @Test
    void 블록_삭제() throws Exception {
        // given
        Long blockId = 1L;
        doNothing().when(blockService).delete(anyString(), anyLong());

        // when & then
        mockMvc.perform(delete("/api/blocks/{blockId}", blockId)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("block/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("blockId").description("블록 ID")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("GET 블록을 상태별로 전체 조회합니다.")
    @Test
    void 블록_상태_전체_조회() throws Exception {
        // given
        String progressString = "NOT_STARTED";
        Page<Block> blockPage = new PageImpl<>(List.of(block), PageRequest.of(0, 10), 1);
        BlockListResDto response = BlockListResDto.from(
                Collections.singletonList(BlockInfoResDto.from(block)),
                PageInfoResDto.from(blockPage));

        given(blockService.findForBlockByProgress(anyString(), anyLong(), anyString(), any())).willReturn(response);

        // when & then
        mockMvc.perform(
                        get(String.format("/api/blocks?dashboardId=%d&progress=%s&page=%d&size=%d", 1L, progressString, 0, 10))
                                .header("Authorization", "Bearer valid-token")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("block/findByBlockWithProgress",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("dashboardId")
                                        .description("대시보드 아이디"),
                                parameterWithName("progress")
                                        .description("블록 상태 문자열(NOT_STARTED, IN_PROGRESS, COMPLETED)"),
                                parameterWithName("page")
                                        .description("페이지 번호"),
                                parameterWithName("size")
                                        .description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.blockListResDto[].blockId").description("블록 아이디"),
                                fieldWithPath("data.blockListResDto[].title").description("블록 제목"),
                                fieldWithPath("data.blockListResDto[].contents").description("블록 내용"),
                                fieldWithPath("data.blockListResDto[].progress").description("블록 진행 상태"),
                                fieldWithPath("data.blockListResDto[].type").description(
                                        "블록 타입(일반(General) 블록인지 챌린지(Challenge) 블록인지 구별)"),
                                fieldWithPath("data.blockListResDto[].dType").description("개인 대시보드, 팀 대시보드를 구별"),
                                fieldWithPath("data.blockListResDto[].startDate").description("블록 시작 시간"),
                                fieldWithPath("data.blockListResDto[].deadLine").description("블록 마감 시간"),
                                fieldWithPath("data.blockListResDto[].nickname").description("회원 닉네임"),
                                fieldWithPath("data.blockListResDto[].picture").description("회원 사진"),
                                fieldWithPath("data.blockListResDto[].dDay").description("마감 기한"),
                                fieldWithPath("data.pageInfoResDto.currentPage").description("현재 페이지"),
                                fieldWithPath("data.pageInfoResDto.totalPages").description("전체 페이지"),
                                fieldWithPath("data.pageInfoResDto.totalItems").description("전체 아이템")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("GET 블록을 상세봅니다.")
    @Test
    void 블록_상세보기() throws Exception {
        // given
        BlockInfoResDto response = BlockInfoResDto.from(block);

        given(blockService.findById(anyString(), anyLong())).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/blocks/{blockId}", 1L)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("block/findById",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("blockId").description("블록 ID")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.blockId").description("블록 아이디"),
                                fieldWithPath("data.title").description("블록 제목"),
                                fieldWithPath("data.contents").description("블록 내용"),
                                fieldWithPath("data.progress").description("블록 진행 상태"),
                                fieldWithPath("data.type").description(
                                        "블록 타입(일반(General) 블록인지 챌린지(Challenge) 블록인지 구별)"),
                                fieldWithPath("data.dType").description("개인 대시보드, 팀 대시보드를 구별"),
                                fieldWithPath("data.startDate").description("블록 시작 시간"),
                                fieldWithPath("data.deadLine").description("블록 마감 시간"),
                                fieldWithPath("data.nickname").description("회원 닉네임"),
                                fieldWithPath("data.picture").description("회원 사진"),
                                fieldWithPath("data.dDay").description("마감 기한")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("PATCH 블록의 순번을 변경합니다.")
    @Test
    void 블록_순번_변경() throws Exception {
        // given
        doNothing().when(blockService).changeBlocksSequence(anyString(), any());

        // when & then
        mockMvc.perform(patch("/api/blocks/change")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blockSequenceUpdateReqDto)))
                .andDo(print())
                .andDo(document("block/change",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("dashboardId").description("대시보드 ID"),
                                fieldWithPath("notStartedList").description("시작 전 블록 아이디 리스트"),
                                fieldWithPath("inProgressList").description("진행 중 블록 아이디 리스트"),
                                fieldWithPath("completedList").description("완료 블록 아이디 리스트")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("GET 삭제된 블록을 조회합니다.")
    @Test
    void 삭제_블록_조회() throws Exception {
        // given
        block.statusUpdate();
        Page<Block> blockPage = new PageImpl<>(List.of(block), PageRequest.of(0, 10), 1);
        BlockListResDto response = BlockListResDto.from(
                Collections.singletonList(BlockInfoResDto.from(block)),
                PageInfoResDto.from(blockPage));

        given(blockService.findDeletedBlocks(anyString(), anyLong(), any())).willReturn(response);

        // when & then
        mockMvc.perform(
                        get(String.format("/api/blocks/deleted?dashboardId=%d&page=%d&size=%d", 1L, 0, 10))
                                .header("Authorization", "Bearer valid-token")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("block/findDeletedBlocks",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("dashboardId")
                                        .description("대시보드 아이디"),
                                parameterWithName("page")
                                        .description("페이지 번호"),
                                parameterWithName("size")
                                        .description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.blockListResDto[].blockId").description("블록 아이디"),
                                fieldWithPath("data.blockListResDto[].title").description("블록 제목"),
                                fieldWithPath("data.blockListResDto[].contents").description("블록 내용"),
                                fieldWithPath("data.blockListResDto[].progress").description("블록 진행 상태"),
                                fieldWithPath("data.blockListResDto[].type").description(
                                        "블록 타입(일반(General) 블록인지 챌린지(Challenge) 블록인지 구별)"),
                                fieldWithPath("data.blockListResDto[].dType").description("개인 대시보드, 팀 대시보드를 구별"),
                                fieldWithPath("data.blockListResDto[].startDate").description("블록 시작 시간"),
                                fieldWithPath("data.blockListResDto[].deadLine").description("블록 마감 시간"),
                                fieldWithPath("data.blockListResDto[].nickname").description("회원 닉네임"),
                                fieldWithPath("data.blockListResDto[].picture").description("회원 사진"),
                                fieldWithPath("data.blockListResDto[].dDay").description("마감 기한"),
                                fieldWithPath("data.pageInfoResDto.currentPage").description("현재 페이지"),
                                fieldWithPath("data.pageInfoResDto.totalPages").description("전체 페이지"),
                                fieldWithPath("data.pageInfoResDto.totalItems").description("전체 아이템")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("DELETED 블록을 영구 삭제 합니다.")
    @Test
    void 블록_영구_삭제() throws Exception {
        // given
        doNothing().when(blockService).deletePermanently(anyString(), any());

        // when & then
        mockMvc.perform(delete("/api/blocks/permanent/{blockId}", 1L)
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("block/deletePermanentBlock",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("blockId").description("블록 ID")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("DELETED 논리 삭제된 블록을 전체 삭제합니다.")
    @Test
    void 삭제_블록_전체_삭제() throws Exception {
        // given
        doNothing().when(blockService).deleteAllPermanently(anyString(), any());

        // when & then
        mockMvc.perform(delete(String.format("/api/blocks/permanent?dashboardId=%d", 1L))
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("block/deleteAllPermanentBlock",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("dashboardId").description("대시보드 ID")
                        )
                ))
                .andExpect(status().isOk());
    }

}