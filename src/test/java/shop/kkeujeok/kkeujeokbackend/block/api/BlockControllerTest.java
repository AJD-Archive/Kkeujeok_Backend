package shop.kkeujeok.kkeujeokbackend.block.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.createRestDocsHandlerWithFields;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.requestFields;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.responseFields;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.exception.InvalidProgressException;
import shop.kkeujeok.kkeujeokbackend.common.annotation.ControllerTest;
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
        blockSaveReqDto = new BlockSaveReqDto("Title", "Contents", Progress.NOT_STARTED);
        blockUpdateReqDto = new BlockUpdateReqDto("UpdateTitle", "UpdateContents");
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
                                fieldWithPath("progress").description("블록 진행 상태")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.title").description("블록 제목"),
                                fieldWithPath("data.contents").description("블록 내용"),
                                fieldWithPath("data.progress").description("블록 진행 상태"),
                                fieldWithPath("data.nickname").description("회원 닉네임")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("Patch 블록 수정 컨트롤러 로직 확인")
    @Test
    void 블록_수정() throws Exception {
        // given
        block.update(blockUpdateReqDto.title(), blockUpdateReqDto.contents());
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
                                fieldWithPath("contents").description("블록 내용")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.title").description("블록 제목"),
                                fieldWithPath("data.contents").description("블록 내용"),
                                fieldWithPath("data.progress").description("블록 진행 상태"),
                                fieldWithPath("data.nickname").description("회원 닉네임")
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
                                fieldWithPath("data.nickname").description("회원 닉네임")
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

}