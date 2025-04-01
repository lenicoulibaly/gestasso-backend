package rigeldevsolutions.gestasso.authmodule.controller.services.spec;

import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakRole;

public interface IUserProfileService
{
    void addProfileToUser(String userId, String profileId, Long assoId, Long sectionId);
    void removeProfileToUser(String userId, String profileId, Long assoId, Long sectionId);
    void setProfileAsDefaultForUser(String userId, String profileId, Long assoId, Long sectionId);
    KeycloakRole getUserActiveProfile(String userId);
}
