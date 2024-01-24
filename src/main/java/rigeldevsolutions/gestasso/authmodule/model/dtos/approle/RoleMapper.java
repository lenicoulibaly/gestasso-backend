package rigeldevsolutions.gestasso.authmodule.model.dtos.approle;

import rigeldevsolutions.gestasso.authmodule.model.entities.AppRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper
{
    AppRole mapToRole(CreateRoleDTO dto);
    ReadRoleDTO mapToReadRoleDTO(AppRole role);
}
