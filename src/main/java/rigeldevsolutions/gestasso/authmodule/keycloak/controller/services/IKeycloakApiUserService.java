package rigeldevsolutions.gestasso.authmodule.keycloak.controller.services;

import org.springframework.data.domain.Page;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser;

public interface IKeycloakApiUserService
{
    Page<KeycloakUser> searchUsers(String key, int page, int size);
    KeycloakUser addUser(KeycloakUser user);
    String getAdminAccessToken();
}
