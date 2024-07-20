package shop.kkeujeok.kkeujeokbackend.block.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockSaveReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.request.BlockUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.application.BlockService;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
import shop.kkeujeok.kkeujeokbackend.block.exception.InvalidProgressException;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;

@WebMvcTest(BlockController.class)
@MockBean(JpaMetamodelMappingContext.class)
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("블록 수정"))
                .andExpect(jsonPath("$.data.title").value("UpdateTitle"))
                .andExpect(jsonPath("$.data.contents").value("UpdateContents"))
                .andExpect(jsonPath("$.data.progress").value("NOT_STARTED"))
                .andExpect(jsonPath("$.data.nickname").value("웅이"));
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
        mockMvc.perform(patch("/api/blocks/{blockId}/progress", blockId)
                        .param("progress", progressString)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("블록 상태 수정"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("Patch 블록 상태 수정 실패 컨트롤러 로직 확인(400 Bad Request가 발생한다)")
    @Test
    void 블록_상태_수정_실패() throws Exception {
        // given
        Long blockId = 1L;
        String progressString = "STATUS_PROGRESS";

        given(blockService.progressUpdate(anyLong(), anyString())).willThrow(new InvalidProgressException());

        // when & then
        mockMvc.perform(patch("/api/blocks/{blockId}/progress", blockId)
                        .param("progress", progressString)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest());
    }
}