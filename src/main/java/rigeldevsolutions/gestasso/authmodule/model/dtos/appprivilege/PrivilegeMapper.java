package rigeldevsolutions.gestasso.authmodule.model.dtos.appprivilege;

import rigeldevsolutions.gestasso.authmodule.model.entities.AppPrivilege;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper
{
    @Mapping(target = "prvType.uniqueCode", source = "typeCode")
    AppPrivilege mapToPrivilege(CreatePrivilegeDTO dto);
    @Mapping(target = "prvTypeName", source = "prvType.name")
    @Mapping(target = "typeCode", source = "prvType.uniqueCode")
    ReadPrvDTO mapToReadPrivilegeDTO(AppPrivilege privilege);
}