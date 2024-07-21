package shop.kkeujeok.kkeujeokbackend.auth.application;

import com.fasterxml.jackson.databind.JsonNode;
import shop.kkeujeok.kkeujeokbackend.auth.api.dto.response.UserInfo;

public interface AuthService {
    UserInfo getUserInfo(String authCode);

    String getProvider();

    JsonNode getIdToken(String code);
}
