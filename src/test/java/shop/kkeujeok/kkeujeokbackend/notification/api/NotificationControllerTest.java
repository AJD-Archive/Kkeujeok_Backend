package shop.kkeujeok.kkeujeokbackend.notification.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.kkeujeok.kkeujeokbackend.global.restdocs.RestDocsHandler.responseFields;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.request.TokenReqDto;
import shop.kkeujeok.kkeujeokbackend.common.annotation.ControllerTest;
import shop.kkeujeok.kkeujeokbackend.global.annotationresolver.CurrentUserEmailArgumentResolver;
import shop.kkeujeok.kkeujeokbackend.global.entity.Status;
import shop.kkeujeok.kkeujeokbackend.global.error.ControllerAdvice;
import shop.kkeujeok.kkeujeokbackend.member.domain.Member;
import shop.kkeujeok.kkeujeokbackend.member.domain.Role;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;
import shop.kkeujeok.kkeujeokbackend.notification.api.dto.response.NotificationInfoResDto;
import shop.kkeujeok.kkeujeokbackend.notification.api.dto.response.NotificationListResDto;
import shop.kkeujeok.kkeujeokbackend.notification.application.NotificationService;
import shop.kkeujeok.kkeujeokbackend.notification.domain.Notification;

@ExtendWith(RestDocumentationExtension.class)
class NotificationControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer valid-token";

    private Member member;
    private Notification notification;
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        member = Member.builder()
                .status(Status.ACTIVE)
                .email("kkeujeok@gmail.com")
                .name("김동균")
                .picture("기본 프로필")
                .socialType(SocialType.GOOGLE)
                .role(Role.ROLE_USER)
                .firstLogin(true)
                .nickname("동동")
                .build();

        notification = Notification.builder()
                .receiver(member)
                .message("테스트 알림")
                .isRead(false)
                .build();

        ReflectionTestUtils.setField(notification, "id", 1L);

        mockMvc = MockMvcBuilders.standaloneSetup(notificationController)
                .apply(documentationConfiguration(restDocumentation))
                .setCustomArgumentResolvers(new CurrentUserEmailArgumentResolver(tokenProvider))
                .setControllerAdvice(new ControllerAdvice()).build();

        when(tokenProvider.getUserEmailFromToken(any(TokenReqDto.class))).thenReturn("kkeujeok@gmail.com");
    }

    @Test
    @DisplayName("사용자는 SSE 발행기를 생성시 상태코드 200 반환.")
    void 사용자는_SSE_발행기를_생성시_상태코드_200_반환() throws Exception {
        // given
        given(notificationService.createEmitter(member.getEmail())).willReturn(emitter);

        // when & then
        mockMvc.perform(get("/api/notifications/stream")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(member.getEmail()))
                .andDo(print())
                .andDo(document("notification/stream",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 토큰")
                        ))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("알림 전체 조회에 성공하면 상태코드 200 반환")
    void 알림_전체_조회에_성공하면_상태코드_200_반환() throws Exception {
        // given
        NotificationInfoResDto notificationInfoResDto = NotificationInfoResDto.from(notification);
        NotificationListResDto response = NotificationListResDto.of(List.of(notificationInfoResDto));

        given(notificationService.findAllNotificationsFromMember(member.getEmail()))
                .willReturn(response);

        // when & then
        mockMvc.perform(
                        get("/api/notifications")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(member.getEmail()))
                .andDo(print())
                .andDo(document("notification/findAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 토큰")),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.notificationInfoResDto[].id").description("알림 아이디"),
                                fieldWithPath("data.notificationInfoResDto[].message").description("알림 메시지"),
                                fieldWithPath("data.notificationInfoResDto[].isRead").description("알림 읽은 여부")
                        ))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("모든 알림 읽음으로 바꾸는데 성공하면 상태코드 200 반환")
    void 모든_알림_읽음으로_바꾸는데_성공하면_상태코드_200_반환() throws Exception {
        // given
        // 모든 알림을 읽음 처리하는 메서드 호출에 대한 설정
        willDoNothing().given(notificationService).markAllNotificationsAsRead(member.getEmail());

        // when & then
        mockMvc.perform(
                        patch("/api/notifications")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("notification/markAsAllRead",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 토큰"))
                ))
                .andExpect(status().isOk());
    }


    /*@Test
    @DisplayName("알림 상세 조회에 성공하면 상태코드 200 반환")
    void 알림_상세_조회에_성공하면_상태코드_200_반환() throws Exception {
        // given
        NotificationInfoResDto notificationInfoResDto = NotificationInfoResDto.from(notification);
        given(notificationService.findByNotificationId(anyLong())).willReturn(notificationInfoResDto);

        // when & then
        mockMvc.perform(
                        get("/api/notifications/{notificationId}", 1L)
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                                .content(member.getEmail()))
                .andDo(print())
                .andDo(document("notification/findById",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("notificationId").description("알림 ID")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("상태 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("알림 아이디"),
                                fieldWithPath("data.message").description("알림 메시지"),
                                fieldWithPath("data.isRead").description("알림 읽은 여부")
                        ))
                )
                .andExpect(status().isOk());
    }*/
}
