package shop.kkeujeok.kkeujeokbackend.auth.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthServiceFactory {
    private final Map<String, AuthService> authServiceMap;

    @Autowired
    public AuthServiceFactory(List<AuthService> authServiceList) {
        authServiceMap = new HashMap<>();
        for (AuthService authService : authServiceList) {
            authServiceMap.put(authService.getProvider(), authService);
        }
    }

    public AuthService getAuthService(String provider) {
        return authServiceMap.get(provider);
    }
}
