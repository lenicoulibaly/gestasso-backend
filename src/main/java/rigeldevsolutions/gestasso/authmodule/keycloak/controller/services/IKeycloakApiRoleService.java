package rigeldevsolutions.gestasso.authmodule.keycloak.controller.services;

import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakRole;

import java.util.List;

public interface IKeycloakApiRoleService
{
    void addProfileToUser(KeycloakRole profile, String userId);
    void removeProfileToUser(KeycloakRole profile, String userId);
    void saveProfile(String profileName, String description, List<String> roleNames);
    void saveRole(String roleName, String description, List<String> privileges);
    void savePrivilege(String privilegeName, String description);


    boolean roleHasePrivilege(String roleName, String privilegeName);
    void addSubRoleToRole(KeycloakRole privilege, KeycloakRole role);
    void removeSubRoleToRole(KeycloakRole privilege, KeycloakRole role);
}
