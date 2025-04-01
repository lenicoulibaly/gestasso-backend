package rigeldevsolutions.gestasso.authmodule.keycloak.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories.KeycloakRoleRepo;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories.KeycloakUserRepo;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories.RecursiveRoleRepo;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.dtos.KeycloakAttribute;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakRole;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.env.KeycloakEnv;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.WebClientUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class KeycloakApiRoleService implements IKeycloakApiRoleService
{
    private final KeycloakEnv env;
    private final WebClientUtils webClientUtils;
    private final RecursiveRoleRepo rrRepo;
    private final KeycloakRoleRepo krRepo;
    private final KeycloakUserRepo kuRepo;

    private void checkRoleExistence(String roleId)
    {
        if(!krRepo.existsById(roleId)) throw new AppException("Role inconnu");
    }

    private void checkUserExistence(String userId)
    {
        if(!kuRepo.existsById(userId)) throw new AppException("Utilisateur inconnu");
    }

    @Override
    public void addProfileToUser(KeycloakRole profile, String userId)
    {
        this.checkRoleExistence(profile.getId());
        this.checkUserExistence(userId);
        this.addOrRemoveProfileToUser(profile, userId, HttpMethod.POST);
    }

    @Override
    public void removeProfileToUser(KeycloakRole profile, String userId)
    {
        this.checkRoleExistence(profile.getId());
        this.checkUserExistence(userId);
        this.addOrRemoveProfileToUser(profile, userId, HttpMethod.DELETE);
    }

    @Override
    public void addSubRoleToRole(KeycloakRole subRole, KeycloakRole role)
    {
        this.checkRoleExistence(subRole.getId());
        this.checkRoleExistence(role.getId());
        if(this.roleHasePrivilege(role.getName(), subRole.getName())) return;
        String url = "/admin/realms/"+env.keycloakRealm + "/clients/" + env.keycloakClientUuid +"/roles/" + role.getName()+ "/composites";
        webClientUtils.sendHttpRequest(url, subRole, HttpMethod.POST, Void.class);
    }

    @Override
    public void removeSubRoleToRole(KeycloakRole subRole, KeycloakRole role)
    {
        this.checkRoleExistence(subRole.getId());
        this.checkRoleExistence(role.getId());
        if(!this.roleHasePrivilege(role.getName(), subRole.getName())) return;
        String url = "/admin/realms/"+env.keycloakRealm + "/clients/" + env.keycloakClientUuid +"/roles/" + role.getName()+ "/composites";
        webClientUtils.sendHttpRequest(url, subRole, HttpMethod.DELETE, Void.class);
    }

    @Override @Transactional
    public void saveRole(String roleName, String description, List<String> privilegeNames)
    {
        Optional<KeycloakRole> role$ = krRepo.findByClientUuIdAndRoleName(env.keycloakClientUuid, roleName);

        if(!role$.isPresent()) //Si le rôle n'existe pas, on le crée via l'api de keycloak
        {
            addNewAuthority(roleName, description, "ROLE");
        }
        else
        {
            KeycloakRole role = role$.get();
            role.setDescription(description);
            krRepo.save(role);
        }
        updateSubRoles(roleName, privilegeNames, role$);
    }

    @Override
    public void saveProfile(String profileName, String description, List<String> roleNames)
    {
        Optional<KeycloakRole> profile$ = krRepo.findByClientUuIdAndRoleName(env.keycloakClientUuid, profileName);

        if(!profile$.isPresent()) //Si le rôle n'existe pas, on le crée via l'api de keycloak
        {
            addNewAuthority(profileName, description, "PROFILE");
        }
        else
        {
            KeycloakRole profile = profile$.get();
            profile.setDescription(description);
            krRepo.save(profile);
        }
        updateSubRoles(profileName, roleNames, profile$);
    }

    private void updateSubRoles(String roleName, List<String> subRolesNames, Optional<KeycloakRole> role$) {
        KeycloakRole finalRole = role$.get();
        if(subRolesNames == null || subRolesNames.isEmpty()) return;

        List<String> rolesNamesToAdd = this.getSubRolesNamesToAdd(roleName, subRolesNames);
        List<String> rolesNamesToRemove = this.getSubRolesNamesToRemove(roleName, subRolesNames);

        List<KeycloakRole> rolesToRemove = krRepo.findByClientUuIdAndRoleNames(env.keycloakClientUuid, rolesNamesToRemove);
        List<KeycloakRole> rolesToAdd = krRepo.findByClientUuIdAndRoleNames(env.keycloakClientUuid, rolesNamesToAdd);
        rolesToRemove.forEach(p->this.removeSubRoleToRole(p, finalRole));
        rolesToAdd.forEach(p->this.addSubRoleToRole(p, finalRole));
    }

    private void addNewAuthority(String profileName, String description, String type) {
        Map<String, List<String>> typeAttribute = new HashMap<>();
        typeAttribute.put("type", Collections.singletonList(type));
        KeycloakRole profile = new KeycloakRole(profileName, description, new KeycloakAttribute(typeAttribute));
        String url = "/admin/realms/" + env.keycloakRealm + "/clients/" + env.keycloakClientUuid + "/roles";
        webClientUtils.sendHttpRequest(url, profile, HttpMethod.POST, Void.class);
    }

    @Override
    public void savePrivilege(String privilegeName, String description) {

    }

    @Override
    public boolean roleHasePrivilege(String roleName, String privilegeName)
    {
        String rolePath = rrRepo.findRolePathByClientIdAndRoleName(roleName, env.keycloakClientUuid);
        String privilegePath = rrRepo.findRolePathByClientIdAndRoleName(privilegeName, env.keycloakClientUuid);
        if(rolePath == null || privilegePath == null) return false;
        return privilegePath.startsWith(rolePath);
    }

    private void addOrRemoveProfileToUser(KeycloakRole role, String userId, HttpMethod method)
    {
        String url = "/admin/realms/" + env.keycloakRealm + "/users/" + userId + "/role-mappings/clients/" + env.keycloakClientUuid;
        webClientUtils.sendHttpRequest(url, role, method, Void.class);
    }

    public List<String> findAllSubAuthoritiesNames(String parentName)
    {
        String rolePath = rrRepo.findRolePathByClientIdAndRoleName(env.keycloakClientUuid, parentName);
        List<String> subRoleNames = rrRepo.findAllSubRoleNamesByRolePath(env.keycloakClientUuid, rolePath);
        return subRoleNames;
    }

    private List<String> getSubRolesNamesToRemove(String roleName, List<String> givenSubRolesNames)
    {//Privilèges que le role possède mais qui ne sont pas venus (owned - given)
        if(roleName == null || roleName.trim().equals("")) return Collections.emptyList();
        List<String> ownedPrivilegeNames = findAllSubAuthoritiesNames( roleName);
        if(ownedPrivilegeNames == null || ownedPrivilegeNames.isEmpty()) return Collections.emptyList();
        if(givenSubRolesNames != null && givenSubRolesNames.isEmpty()) return ownedPrivilegeNames;
        if(givenSubRolesNames == null) return Collections.emptyList();
        ownedPrivilegeNames.removeAll(givenSubRolesNames);
        return ownedPrivilegeNames;
    }

    private List<String> getSubRolesNamesToAdd(String roleName, List<String> givenPrivilegeNames)
    {//Privilèges venus (donnés) mais que le role ne possède pas (given - owned)
        if(roleName == null || roleName.trim().equals("") || givenPrivilegeNames == null || givenPrivilegeNames.isEmpty()) return Collections.emptyList();
        List<String> ownedPrivilegeNames = findAllSubAuthoritiesNames( roleName);
        if(ownedPrivilegeNames == null || ownedPrivilegeNames.isEmpty()) return givenPrivilegeNames;
                List<String> privilegeNamesToAdd = givenPrivilegeNames.stream()
                .filter(item -> !ownedPrivilegeNames.contains(item))
                .collect(Collectors.toList());
        return privilegeNamesToAdd;
    }
}