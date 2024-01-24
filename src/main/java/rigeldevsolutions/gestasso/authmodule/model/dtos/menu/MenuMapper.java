package rigeldevsolutions.gestasso.authmodule.model.dtos.menu;

import rigeldevsolutions.gestasso.authmodule.model.entities.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuMapper
{
    @Mapping(target = "status", expression = "java(rigeldevsolutions.gestasso.sharedmodule.enums.PersStatus.ACTIVE)")
    Menu mapToMenu(CreateMenuDTO dto);
}