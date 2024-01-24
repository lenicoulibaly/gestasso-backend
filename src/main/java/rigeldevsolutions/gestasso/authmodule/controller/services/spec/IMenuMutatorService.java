package rigeldevsolutions.gestasso.authmodule.controller.services.spec;

import rigeldevsolutions.gestasso.authmodule.model.dtos.menu.CreateMenuDTO;
import rigeldevsolutions.gestasso.authmodule.model.dtos.menu.PrvToMenuDTO;
import rigeldevsolutions.gestasso.authmodule.model.entities.Menu;

import java.net.UnknownHostException;

public interface IMenuMutatorService
{

    Menu createMenu(CreateMenuDTO dto) throws UnknownHostException;
    void addPrvToMenu(PrvToMenuDTO dto) throws UnknownHostException;
    void removePrvToMenu(PrvToMenuDTO dto) throws UnknownHostException;
}
