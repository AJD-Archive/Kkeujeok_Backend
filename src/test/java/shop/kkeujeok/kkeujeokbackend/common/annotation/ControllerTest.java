package shop.kkeujeok.kkeujeokbackend.common.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import shop.kkeujeok.kkeujeokbackend.block.api.BlockController;
import shop.kkeujeok.kkeujeokbackend.block.application.BlockService;

@AutoConfigureRestDocs
@WebMvcTest({
        BlockController.class
})
@ActiveProfiles("test")
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected BlockService blockService;
}
