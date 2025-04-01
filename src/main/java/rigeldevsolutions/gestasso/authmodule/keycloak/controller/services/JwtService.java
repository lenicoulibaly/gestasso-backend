package rigeldevsolutions.gestasso.authmodule.keycloak.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.dtos.JwtInfos;

import java.util.List;
import java.util.Map;

@Service @RequiredArgsConstructor
public class JwtService implements IJwtService
{
    @Value("${keycloak.client.id}")
    private String clientId;

    @Override
    public String extractUsername()
    {
        String extractedUsername = this.extractUsername(this.getCurrentJwt());
        return extractedUsername == null || extractedUsername.trim().equals("") ? "UNKNOWN" : extractedUsername;
    }
    public String getUsername(Jwt jwt)
    {
        return jwt == null ? "" : jwt.getClaimAsString("preferred_username");
    }

    @Override
    public String extractConnectionId()
    {
        Jwt jwt = this.getCurrentJwt();
        return jwt == null ? "" : jwt.getClaimAsString("jti");
    }

    @Override
    public String extractUsername(Jwt jwt)
    {
        return jwt == null ? "" : jwt.getClaimAsString("preferred_username");
    }

    @Override
    public JwtInfos getJwtInfos() {
        return this.getJwtInfos(this.getCurrentJwt());
    }

    @Override
    public JwtInfos getJwtInfos(Jwt jwt)
    {
        if(jwt == null) return null;
        JwtInfos jwtInfos = new JwtInfos();

        jwtInfos.setUserEmail(jwt.getClaimAsString("email"));
        jwtInfos.setUserId(jwt.getSubject());
        jwtInfos.setAssoId(jwt.getClaim("assoId"));
        jwtInfos.setAssoName(jwt.getClaim("assoName"));
        jwtInfos.setSectionId(jwt.getClaim("sectionId"));
        jwtInfos.setSectionName(jwt.getClaim("sectionName"));
        jwtInfos.setStrId(jwt.getClaim("strId"));
        jwtInfos.setStrName(jwt.getClaim("strName"));
        jwtInfos.setStrCode(jwt.getClaim("strCode"));
        jwtInfos.setStrSigle(jwt.getClaim("strSigle"));
        jwtInfos.setAuthorities(this.getAuthorities());
        jwtInfos.setConnectionId(this.extractConnectionId());

        return jwtInfos;
    }

    @Override
    public Jwt getCurrentJwt()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt; // Retourne le JWT brut
        }
        return null;
    }

    @Override
    public String getCurrentAccessToken()
    {
        Jwt jwt = this.getCurrentJwt();
        if(jwt == null) return null;
        return jwt.getTokenValue();
    }

    @Override
    public String getConnectedUserId()
    {
        Jwt jwt= this.getCurrentJwt();
        return jwt == null ? null : jwt.getClaimAsString("sub");
    }

    @Override
    public List<String> getAuthorities()
    {
        Jwt jwt = this.getCurrentJwt();
        return this.getGestassoRoles(jwt);
    }

    public List<String> getGestassoRoles(Jwt jwt)
    {
        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        if (resourceAccess != null && resourceAccess.containsKey(clientId))
        {
            Map<String, Object> gestassoBack = (Map<String, Object>) resourceAccess.get(clientId);
            List<String> roles = (List<String>) gestassoBack.get("roles");
            return roles;
        }
        return List.of();
    }
}