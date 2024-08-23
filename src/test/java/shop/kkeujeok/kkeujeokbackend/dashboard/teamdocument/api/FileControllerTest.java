package shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api;

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
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import shop.kkeujeok.kkeujeokbackend.common.annotation.ControllerTest;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.request.FileInfoReqDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response.FileInfoResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.api.dto.response.FileListResDto;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.application.FileService;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.Document;
import shop.kkeujeok.kkeujeokbackend.dashboard.teamdocument.domain.File;
import shop.kkeujeok.kkeujeokbackend.global.annotationresolver.CurrentUserEmailArgumentResolver;
import shop.kkeujeok.kkeujeokbackend.global.dto.PageInfoResDto;
import shop.kkeujeok.kkeujeokbackend.global.error.ControllerAdvice;

class FileControllerTest extends ControllerTest{

    @InjectMocks
    FileController fileController;
    private Document document;
    private File file;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        fileController = new FileController(fileService);

        mockMvc = MockMvcBuilders.standaloneSetup(fileController)
                .apply(documentationConfiguration(restDocumentation))
                .setCustomArgumentResolvers(new CurrentUserEmailArgumentResolver(tokenProvider))
                .setControllerAdvice(new ControllerAdvice())
                .build();

        document = Document.builder()
                .title("DocumentTitle")
                .build();

        ReflectionTestUtils.setField(document, "id", 1L);

        file = File.builder()
                .email("email")
                .title("FileTitle")
                .content("FileContent")
                .document(document)
                .build();

        when(tokenProvider.getUserEmailFromToken(any())).thenReturn("email");
    }

    @DisplayName("POST 파일 저장 컨트롤러 로직 확인")
    @Test
    void POST_파일_저장_컨트롤러_로직_확인() throws Exception {
        // given
        FileInfoReqDto request = new FileInfoReqDto(1L, "email", "title", "content");
        FileInfoResDto response = new FileInfoResDto(1L, "title", "content", "email");

        given(fileService.save(anyString(), any(FileInfoReqDto.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/files/")
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("file/save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("JWT 토큰")
                        ),
                        requestFields(
                                fieldWithPath("documentId").description("문서 ID"),
                                fieldWithPath("email").description("파일 생성자 이메일"),
                                fieldWithPath("title").description("파일 제목"),
                                fieldWithPath("content").description("파일 내용")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.fileId").description("파일 ID"),
                                fieldWithPath("data.title").description("파일 제목"),
                                fieldWithPath("data.content").description("파일 내용"),
                                fieldWithPath("data.email").description("파일 생성자 이메일")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("PATCH 파일 수정 컨트롤러 로직 확인")
    @Test
    void PATCH_파일_수정_컨트롤러_로직_확인() throws Exception {
        // given
        Long fileId = 1L;
        FileInfoReqDto request = new FileInfoReqDto(1L, "email", "updatedTitle", "updatedContent");
        FileInfoResDto response = new FileInfoResDto(fileId, "updatedTitle", "updatedContent", "email");

        given(fileService.update(anyLong(), any(FileInfoReqDto.class))).willReturn(response);

        // when & then
        mockMvc.perform(patch("/api/files/{fileId}", fileId)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andDo(document("file/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("fileId").description("파일 ID")
                        ),
                        requestFields(
                                fieldWithPath("documentId").description("문서 ID"),
                                fieldWithPath("email").description("파일 생성자 이메일"),
                                fieldWithPath("title").description("파일 제목"),
                                fieldWithPath("content").description("파일 내용")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.fileId").description("파일 ID"),
                                fieldWithPath("data.title").description("파일 제목"),
                                fieldWithPath("data.content").description("파일 내용"),
                                fieldWithPath("data.email").description("파일 생성자 이메일")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("GET 파일 리스트 조회 컨트롤러 로직 확인")
    @Test
    void GET_파일_리스트_조회_컨트롤러_로직_확인() throws Exception {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<File> filePage = new PageImpl<>(List.of(file));

        FileListResDto response = new FileListResDto(
                Collections.singletonList(new FileInfoResDto(1L, "title", "content", "email")),
                PageInfoResDto.from(filePage)
        );

        given(fileService.findForFile(anyLong(), any(PageRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/files")
                        .param("documentId", "1")
                        .param("page", "0")
                        .param("size", "10")
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("file/findForFile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("documentId").description("문서 ID"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.fileInfoResDto[].fileId").description("파일 ID"),
                                fieldWithPath("data.fileInfoResDto[].title").description("파일 제목"),
                                fieldWithPath("data.fileInfoResDto[].content").description("파일 내용"),
                                fieldWithPath("data.fileInfoResDto[].email").description("파일 생성자 이메일"),
                                fieldWithPath("data.pageInfoResDto.currentPage").description("현재 페이지 번호"),
                                fieldWithPath("data.pageInfoResDto.totalPages").description("전체 페이지 수"),
                                fieldWithPath("data.pageInfoResDto.totalItems").description("전체 아이템 수")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("GET 파일 상세보기 컨트롤러 로직 확인")
    @Test
    void GET_파일_상세보기_컨트롤러_로직_확인() throws Exception {
        // given
        Long fileId = 1L;
        FileInfoResDto response = new FileInfoResDto(fileId, "title", "content", "email");

        given(fileService.findById(anyLong())).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/files/{fileId}", fileId)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("file/findById",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("fileId").description("파일 ID")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.fileId").description("파일 ID"),
                                fieldWithPath("data.title").description("파일 제목"),
                                fieldWithPath("data.content").description("파일 내용"),
                                fieldWithPath("data.email").description("파일 생성자 이메일")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("DELETE 파일 삭제 컨트롤러 로직 확인")
    @Test
    void DELETE_파일_삭제_컨트롤러_로직_확인() throws Exception {
        // given
        Long fileId = 1L;
        doNothing().when(fileService).delete(anyLong());

        // when & then
        mockMvc.perform(delete("/api/files/{fileId}", fileId)
                        .header("Authorization", "Bearer valid-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("file/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("fileId").description("파일 ID")
                        )
                ))
                .andExpect(status().isOk());
    }
}
