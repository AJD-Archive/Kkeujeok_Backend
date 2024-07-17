package shop.kkeujeok.kkeujeokbackend.auth.application;

import shop.kkeujeok.kkeujeokbackend.auth.api.dto.response.UserInfo;

public interface AuthService {
    UserInfo getUserInfo(String authCode);

    String getProvider();
}
