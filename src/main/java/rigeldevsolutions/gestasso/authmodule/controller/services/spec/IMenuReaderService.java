package rigeldevsolutions.gestasso.authmodule.controller.services.spec;


import java.util.Set;

public interface IMenuReaderService
{
    boolean menuHasPrv(String menuCode, String prvCode);
    boolean prvCanSeeMenu(String prvCode, String menuCode);
    Set<String> getMenuPrvCodes(String menuCode);
    Set<String> getMenusByProfileId(String profileId);
    boolean profileCanSeeMenu(String profileId, String menuCode);
}
