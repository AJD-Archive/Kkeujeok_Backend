package shop.kkeujeok.kkeujeokbackend.block.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.createRestDocsHandlerWithFields;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.requestFields;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.responseFields;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockListResDto;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.exception.InvalidProgressException;
import shop.kkeujeok.kkeujeokbackend.common.annotation.ControllerTest;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

class BlockControllerTest extends ControllerTest {
    private Member member;
    private Block block;
    private BlockSaveReqDto blockSaveReqDto;
    private BlockUpdateReqDto blockUpdateReqDto;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .nickname("웅이")
                .build();
        blockSaveReqDto = new BlockSaveReqDto("Title", "Contents", Progress.NOT_STARTED, "2024.08.03 13:23");
        blockUpdateReqDto = new BlockUpdateReqDto("UpdateTitle", "UpdateContents", "2024.07.28 16:40");
        block = blockSaveReqDto.toEntity(member);
    }

    @DisplayName("POST 블록 저장 컨트롤러 로직 확인")
    @Test
    void 블록_저장() throws Exception {
        // given
        BlockInfoResDto response = BlockInfoResDto.of(block, member);
        given(blockService.save(any(BlockSaveReqDto.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/blocks/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blockSaveReqDto)))
                .andDo(print())
                .andDo(createRestDocsHandlerWithFields(
                        "block/save",
                        requestFields(
                                fieldWithPath("title").description("블록 제목"),
                                fieldWithPath("contents").description("블록 내용"),
                                fieldWithPath("progress").description("블록 진행 상태"),
                                fieldWithPath("deadLine").description("블록 마감 시간")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.title").description("블록 제목"),
                                fieldWithPath("data.contents").description("블록 내용"),
                                fieldWithPath("data.progress").description("블록 진행 상태"),
                                fieldWithPath("data.deadLine").description("블록 마감 시간"),
                                fieldWithPath("data.nickname").description("회원 닉네임"),
                                fieldWithPath("data.dDay").description("마감 기한")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("Patch 블록 수정 컨트롤러 로직 확인")
    @Test
    void 블록_수정() throws Exception {
        // given
        block.update(blockUpdateReqDto.title(), blockUpdateReqDto.contents(), blockUpdateReqDto.deadLine());
        BlockInfoResDto response = BlockInfoResDto.of(block, member);
        given(blockService.update(anyLong(), any(BlockUpdateReqDto.class))).willReturn(response);

        // when & then
        mockMvc.perform(patch("/api/blocks/{blockId}", 1L)
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
                                fieldWithPath("deadLine").description("블록 마감 시간")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.title").description("블록 제목"),
                                fieldWithPath("data.contents").description("블록 내용"),
                                fieldWithPath("data.progress").description("블록 진행 상태"),
                                fieldWithPath("data.deadLine").description("블록 마감 시간"),
                                fieldWithPath("data.nickname").description("회원 닉네임"),
                                fieldWithPath("data.dDay").description("마감 기한")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("Patch 블록 상태 수정 컨트롤러 로직 확인")
    @Test
    void 블록_상태_수정() throws Exception {
        // given
        Long blockId = 1L;
        String progressString = "IN_PROGRESS";
        BlockInfoResDto response = BlockInfoResDto.of(block, member);

        given(blockService.progressUpdate(anyLong(), anyString())).willReturn(response);

        // when & then
        mockMvc.perform(patch(String.format("/api/blocks/{blockId}/progress?progress=%s", progressString), blockId)
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
                                fieldWithPath("data.title").description("블록 제목"),
                                fieldWithPath("data.contents").description("블록 내용"),
                                fieldWithPath("data.progress").description("블록 진행 상태"),
                                fieldWithPath("data.deadLine").description("블록 마감 시간"),
                                fieldWithPath("data.nickname").description("회원 닉네임"),
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

        given(blockService.progressUpdate(anyLong(), anyString())).willThrow(new InvalidProgressException());

        // when & then
        mockMvc.perform(patch(String.format("/api/blocks/{blockId}/progress?progress=%s", progressString), blockId)
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

    @DisplayName("Delete 블록을 논리적으로 삭제합니다.")
    @Test
    void 블록_삭제() throws Exception {
        // given
        Long blockId = 1L;
        doNothing().when(blockService).delete(anyLong());

        // when & then
        mockMvc.perform(delete("/api/blocks/{blockId}", blockId)
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
                Collections.singletonList(BlockInfoResDto.of(block, member)),
                PageInfoResDto.from(blockPage));

        given(blockService.findByBlockWithProgress(anyString(), any())).willReturn(response);

        // when & then
        mockMvc.perform(get(String.format("/api/blocks?progress=%s", progressString))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("block/findByBlockWithProgress",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("progress")
                                        .description("블록 상태 문자열(NOT_STARTED, IN_PROGRESS, COMPLETED)")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.blockListResDto[].title").description("블록 제목"),
                                fieldWithPath("data.blockListResDto[].contents").description("블록 내용"),
                                fieldWithPath("data.blockListResDto[].progress").description("블록 진행 상태"),
                                fieldWithPath("data.blockListResDto[].deadLine").description("블록 마감 시간"),
                                fieldWithPath("data.blockListResDto[].nickname").description("회원 닉네임"),
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
        BlockInfoResDto response = BlockInfoResDto.of(block, member);

        given(blockService.findById(anyLong())).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/blocks/{blockId}", 1L)
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
                                fieldWithPath("data.title").description("블록 제목"),
                                fieldWithPath("data.contents").description("블록 내용"),
                                fieldWithPath("data.progress").description("블록 진행 상태"),
                                fieldWithPath("data.deadLine").description("블록 마감 시간"),
                                fieldWithPath("data.nickname").description("회원 닉네임"),
                                fieldWithPath("data.dDay").description("마감 기한")
                        )
                ))
                .andExpect(status().isOk());
    }

}