package rigeldevsolutions.gestasso.authmodule.keycloak.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories.KeycloakRoleRepo;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories.KeycloakUserRepo;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakRole;
import rigeldevsolutions.gestasso.authmodule.keycloak.model.entities.KeycloakUser;
import rigeldevsolutions.gestasso.sharedmodule.utilities.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController @RequiredArgsConstructor
public class KeycloakRestController
{
    private final KeycloakUserRepo userRepo;
    private final KeycloakRoleRepo roleRepo;
    @Value("${keycloak.client.uuid}")
    private String clientUuid;

    @GetMapping(path = "/search")
    Page<KeycloakUser> search(@RequestParam(defaultValue = "", required = false)String key,
                              @RequestParam(defaultValue = "0", required = false) int page,
                              @RequestParam(defaultValue = "10", required = false) int size)
    {
        return userRepo.search(key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/roles/{type}")
    List<KeycloakRole> getRolesByType(@PathVariable(required = true) String type)
    {
        type = StringUtils.stripAccentsToUpperCase(type);
        return roleRepo.findByClientUuIdAndRoleType(clientUuid, type);
    }

    @GetMapping(path = "/roles")
    List<KeycloakRole> getRolesByRoleNames(@RequestParam(required = false) List<String> roleNames)
    {
        roleNames = Optional.ofNullable(roleNames).orElse(Collections.emptyList());
        return roleRepo.findByClientUuIdAndRoleNames(clientUuid, roleNames);
    }
}
