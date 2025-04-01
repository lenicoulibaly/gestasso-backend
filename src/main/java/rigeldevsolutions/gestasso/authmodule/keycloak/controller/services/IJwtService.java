package rigeldevsolutions.gestasso.authmodule.keycloak.controller.services;

import org.springframework.security.oauth2.jwt.Jwt;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.dtos.JwtInfos;

import java.util.List;

public interface IJwtService
{
    String extractUsername();
    String extractConnectionId();

    String extractUsername(Jwt jwt);

    JwtInfos getJwtInfos();
    JwtInfos getJwtInfos(Jwt jwt);

    Jwt getCurrentJwt();
    String getCurrentAccessToken();

    String getConnectedUserId();

    List<String> getAuthorities();
}
