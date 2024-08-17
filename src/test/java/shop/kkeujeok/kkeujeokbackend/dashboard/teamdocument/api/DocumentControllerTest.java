package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.requestFields;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.responseFields;

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
import shop.kkeujeok.kkeujeokbackend.common.annotation.ControllerTest;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.request.DocumentInfoReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.request.DocumentUpdateReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response.DocumentInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response.DocumentListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;
import shop.kkeujeok.kkeujeokbackend.global.annotationresolver.CurrentUserEmailArgumentResolver;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.global.error.ControllerAdvice;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

import java.util.Collections;
import java.util.List;

class DocumentControllerTest extends ControllerTest {

    @InjectMocks
    DocumentController documentController;

    private Document document;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        documentController = new DocumentController(documentService);

        mockMvc = MockMvcBuilders.standaloneSetup(documentController)
                .apply(documentationConfiguration(restDocumentation))
                .setCustomArgumentResolvers(new CurrentUserEmailArgumentResolver(tokenProvider))
                .setControllerAdvice(new ControllerAdvice())
                .build();

        document = Document.builder()
                .title("DocumentTitle")
                .build();

        ReflectionTestUtils.setField(document, "id", 1L);
    }

    @DisplayName("POST 팀 문서 저장 컨트롤러 로직 확인")
    @Test
    void POST_팀_문서_저장_컨트롤러_로직_확인() throws Exception {
        // given
        DocumentInfoReqDto request = new DocumentInfoReqDto(1L, "DocumentTitle");
        DocumentInfoResDto response = new DocumentInfoResDto(1L, "DocumentTitle");

        given(documentService.save(any(DocumentInfoReqDto.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/documents")
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("document/save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("teamDashboardId").description("팀 대시보드 ID"),
                                fieldWithPath("title").description("문서 제목")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.documentId").description("문서 ID"),
                                fieldWithPath("data.title").description("문서 제목")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("PATCH 팀 문서 수정 컨트롤러 로직 확인")
    @Test
    void PATCH_팀_문서_수정_컨트롤러_로직_확인() throws Exception {
        // given
        Long documentId = 1L;
        DocumentUpdateReqDto request = new DocumentUpdateReqDto("Updated Document Title");
        DocumentInfoResDto response = new DocumentInfoResDto(documentId, "Updated Document Title");

        given(documentService.update(anyLong(), any(DocumentUpdateReqDto.class))).willReturn(response);

        // when & then
        mockMvc.perform(patch("/api/documents/{documentId}", documentId)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("document/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("documentId").description("문서 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("문서 제목")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.documentId").description("문서 ID"),
                                fieldWithPath("data.title").description("문서 제목")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("GET 팀 문서 리스트 조회 컨트롤러 로직 확인")
    @Test
    void GET_팀_문서_리스트_조회_컨트롤러_로직_확인() throws Exception {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Document> documentPage = new PageImpl<>(List.of(document));

        DocumentListResDto response = new DocumentListResDto(
                Collections.singletonList(new DocumentInfoResDto(1L, "DocumentTitle")),
                PageInfoResDto.from(documentPage)
        );

        given(documentService.findDocumentByTeamDashboardId(anyLong(), any(PageRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/documents")
                        .param("teamDashboardId", "1")
                        .param("page", "0")
                        .param("size", "10")
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("document/findForDocumentByTeamDashboardId",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("teamDashboardId").description("팀 대시보드 ID"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.documentInfoResDtos[].documentId").description("문서 ID"),
                                fieldWithPath("data.documentInfoResDtos[].title").description("문서 제목"),
                                fieldWithPath("data.pageInfoResDto.currentPage").description("현재 페이지 번호"),
                                fieldWithPath("data.pageInfoResDto.totalPages").description("전체 페이지 수"),
                                fieldWithPath("data.pageInfoResDto.totalItems").description("전체 아이템 수")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("DELETE 팀 문서 삭제 컨트롤러 로직 확인")
    @Test
    void DELETE_팀_문서_삭제_컨트롤러_로직_확인() throws Exception {
        // given
        Long documentId = 1L;
        doNothing().when(documentService).delete(anyLong());

        // when & then
        mockMvc.perform(delete("/api/documents/{documentId}", documentId)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("document/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("documentId").description("문서 ID")
                        )
                ))
                .andExpect(status().isOk());
    }
}
