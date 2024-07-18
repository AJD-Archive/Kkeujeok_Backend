package shop.kkeujeok.kkeujeokbackend.global.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.response.UserInfo;
import shop.kkeujeok.kkeujeokbackend.auth.application.AuthService;
import shop.kkeujeok.kkeujeokbackend.global.oauth.exception.OAuthException;
import shop.kkeujeok.kkeujeokbackend.member.domain.SocialType;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class GoogleAuthService implements AuthService {

    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    @Value("${google.client.id}")
    private String google_client_id;
    @Value("${google.client.secret}")
    private String google_client_secret;
    @Value("${google.redirect.uri}")
    private String GOOGLE_REDIRECT_URI;
    private static final String JWT_DELIMITER = "\\.";

    private final ObjectMapper objectMapper;

    public GoogleAuthService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public JsonNode getGoogleIdToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = Map.of(
                "code", code,
                "scope", "https://www.googleapis.com/auth/userinfo.profile " +
                        "https://www.googleapis.com/auth/userinfo.email",
                "client_id", google_client_id,
                "client_secret", google_client_secret,
                "redirect_uri", GOOGLE_REDIRECT_URI,
                "grant_type", "authorization_code"
        );

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL, params, String.class);

        return parseGoogleIdToken(responseEntity);
    }

    @Override
    public String getProvider() {
        return String.valueOf(SocialType.GOOGLE).toLowerCase();
    }

    @Transactional
    @Override
    public UserInfo getUserInfo(String idToken) {
        String decodePayload = getDecodePayload(idToken);

        try {
            return objectMapper.readValue(decodePayload, UserInfo.class);
        } catch (JsonProcessingException e) {
            throw new OAuthException("id 토큰을 읽을 수 없습니다.");
        }
    }

    private JsonNode parseGoogleIdToken(ResponseEntity<String> responseEntity) {
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseBody = responseEntity.getBody();
            try {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                return jsonNode.get("id_token");
            } catch (Exception e) {
                throw new RuntimeException("ID 토큰을 파싱하는데 실패했습니다.", e);
            }
        }

        throw new RuntimeException("구글 엑세스 토큰을 가져오는데 실패했습니다.");
    }

    private String getDecodePayload(String idToken) {
        String payload = getPayload(idToken);

        return new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
    }

    private String getPayload(String idToken) {
        return idToken.split(JWT_DELIMITER)[1];
    }

}
