package rigeldevsolutions.gestasso.authmodule.keycloak.model.env;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component @Getter
public class KeycloakEnv
{
    @Value("${keycloak.server.address}")
    public String keycloakServerAddress;
    @Value("${keycloak.realm-id}")
    public String keycloakRealmId;
    @Value("${keycloak.realm}")
    public String keycloakRealm;
    @Value("${keycloak.client.id}")
    public String keycloakClientId;
    @Value("${keycloak.client.uuid}")
    public String keycloakClientUuid;
    @Value("${keycloak.client.secret}")
    public String keycloakClientSecret;
    @Value("${keycloak.admin.username}")
    public String keycloakAdminUsername;
    @Value("${keycloak.admin.password}")
    public String keycloakAdminPassword;
}
