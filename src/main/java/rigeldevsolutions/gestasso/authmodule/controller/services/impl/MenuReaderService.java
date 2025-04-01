package rigeldevsolutions.gestasso.authmodule.controller.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.MenuRepo;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IMenuReaderService;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories.KeycloakRoleRepo;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.repositories.RecursiveRoleRepo;
import rigeldevsolutions.gestasso.authmodule.keycloak.controller.services.KeycloakApiRoleService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class MenuReaderService implements IMenuReaderService
{
    private final MenuRepo menuRepo;
    private final KeycloakRoleRepo krRepo;
    private final RecursiveRoleRepo rrRepo;
    private final KeycloakApiRoleService krService;

    @Override
    public boolean menuHasPrv(String menuCode, String prvCode)
    {
        return menuRepo.menuHasPrivilege(menuCode, prvCode);
    }

    @Override
    public boolean prvCanSeeMenu(String prvCode, String menuCode) {
        return menuRepo.menuHasPrivilege(menuCode, prvCode);
    }

    @Override
    public boolean profileCanSeeMenu(String profileId, String menuCode) {
        List<String> profilePrvCodes = krService.findAllSubAuthoritiesNames(profileId);

        Set<String> menuPrvCodes = this.getMenuPrvCodes(menuCode);
        if(profilePrvCodes == null || menuPrvCodes == null) return false;
        profilePrvCodes.retainAll(menuPrvCodes);
        return !profilePrvCodes.isEmpty();
    }

    @Override
    public Set<String> getMenuPrvCodes(String menuCode) {
        String prvCodeChain = menuRepo.getPrvsCodesByMenuCode(menuCode);
        return  prvCodeChain == null ? new HashSet<>() : new HashSet<>(Arrays.asList(prvCodeChain.split(",")));
    }
    @Override
    public Set<String> getMenusByProfileId(String profileId)
    {
        List<String> prvCodes = krService.findAllSubAuthoritiesNames(profileId);
        Set<String> menus = menuRepo.findAll().stream()
                .filter(m->m.getPrvsCodes().stream().anyMatch(menuCode->prvCodes.contains(menuCode)))
                .map(m->"MENU_" + m.getMenuCode())
                .collect(Collectors.toSet());
        return menus;
    }
}
