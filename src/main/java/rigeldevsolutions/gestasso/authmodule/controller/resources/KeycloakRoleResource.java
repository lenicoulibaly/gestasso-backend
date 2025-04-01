package rigeldevsolutions.gestasso.authmodule.controller.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.services.IKeycloakApiRoleService;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakRole;

import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping(path = "/keycloak-habilitations")
public class KeycloakRoleResource
{
    private final IKeycloakApiRoleService roleService;

    @PostMapping(path = "/save-profile")
    public void saveProfile(String profileName, String description, List<String> roleNames)
    {
        roleService.saveProfile(profileName, description, roleNames);
    }

    @PostMapping(path = "/save-role")
    public void saveRole(String roleName, String description, List<String> privilegeNames)
    {
        roleService.saveRole(roleName, description, privilegeNames);
    }

    @PostMapping(path = "/save-privilege")
    public void savePrivilege(String privilegeName, String description)
    {
        roleService.savePrivilege(privilegeName, description);
    }

    @PutMapping(path = "/add-profile-to-user")
    public void addProfileToUser(KeycloakRole profile, String userId)
    {
        roleService.addProfileToUser(profile, userId);
    }

    @PutMapping(path = "/remove-profile-to-user")
    public void removeProfileToUser(KeycloakRole profile, String userId)
    {
        roleService.removeProfileToUser(profile, userId);
    }

    @PutMapping(path = "/set-profile-as-default")
    public void setProfileAsDefaultForToUser(KeycloakRole profile, String userId)
    {
        roleService.removeProfileToUser(profile, userId);
    }
}
