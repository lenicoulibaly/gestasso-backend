package rigeldevsolutions.gestasso.authmodule.model.dtos.menu;

import org.mapstruct.Mapper;
import rigeldevsolutions.gestasso.authmodule.model.entities.Menu;

@Mapper(componentModel = "spring")
public interface MenuMapper
{
    Menu mapToMenu(CreateMenuDTO dto);
}