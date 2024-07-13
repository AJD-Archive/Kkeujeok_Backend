package shop.kkeujeok.kkeujeokbackend.block.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
import shop.kkeujeok.kkeujeokbackend.block.api.dto.response.BlockInfoResDto;
import shop.kkeujeok.kkeujeokbackend.block.application.BlockService;
import shop.kkeujeok.kkeujeokbackend.block.domain.Block;
import shop.kkeujeok.kkeujeokbackend.block.domain.Progress;
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

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .nickname("웅이")
                .build();
        blockSaveReqDto = new BlockSaveReqDto("Title", "Contents", Progress.NOT_STARTED);
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

}