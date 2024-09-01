package shop.kkeujeok.kkeujeokbackend.global.restdocs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.snippet.Snippet;

public class RestDocsHandler {
    public static RestDocumentationResultHandler createRestDocsHandler(String identifier, Snippet... snippets) {
        return document(identifier,
                preprocessRequest(
                        prettyPrint()),
                preprocessResponse(
                        prettyPrint()),
                snippets
        );
    }

    public static RestDocumentationResultHandler createRestDocsHandlerWithFields(
            String identifier,
            Snippet requestFieldsSnippet,
            Snippet responseFieldsSnippet) {
        return document(identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFieldsSnippet,
                responseFieldsSnippet
        );
    }

    public static Snippet requestFields(FieldDescriptor... fields) {
        return PayloadDocumentation.requestFields(fields);
    }

    public static Snippet responseFields(FieldDescriptor... fields) {
        return PayloadDocumentation.responseFields(fields);
    }
}
