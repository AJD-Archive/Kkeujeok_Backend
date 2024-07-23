package shop.kkeujeok.kkeujeokbackend.example;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.createRestDocsHandlerWithFields;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.requestFields;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.responseFields;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import shop.kkeujeok.kkeujeokbackend.block.api.BlockController;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.application.BlockService;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@ActiveProfiles("test")
@AutoConfigureRestDocs
@WebMvcTest(BlockController.class)
class BlockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BlockService blockService;

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
                        .content(objectMapper.writeValueAsString(blockSaveReqDto))
                ).andDo(print())
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.message").value("블럭 생성"))
                .andExpect(jsonPath("$.data.title").value("Title"))
                .andExpect(jsonPath("$.data.contents").value("Contents"))
                .andExpect(jsonPath("$.data.progress").value("NOT_STARTED"))
                .andExpect(jsonPath("$.data.nickname").value("웅이"));
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
                        .content(objectMapper.writeValueAsString(blockUpdateReqDto))
                ).andDo(print())
                .andDo(createRestDocsHandlerWithFields(
                        "block/update",
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("블록 수정"))
                .andExpect(jsonPath("$.data.title").value("UpdateTitle"))
                .andExpect(jsonPath("$.data.contents").value("UpdateContents"))
                .andExpect(jsonPath("$.data.progress").value("NOT_STARTED"))
                .andExpect(jsonPath("$.data.nickname").value("웅이"));
    }
}
