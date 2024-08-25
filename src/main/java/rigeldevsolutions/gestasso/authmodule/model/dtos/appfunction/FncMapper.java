package rigeldevsolutions.gestasso.authmodule.model.dtos.appfunction;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.PrvRepo;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.RoleToFunctionAssRepo;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.UserRepo;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IMenuReaderService;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appprivilege.PrivilegeMapper;
import rigeldevsolutions.gestasso.authmodule.model.dtos.approle.RoleMapper;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppFunction;

@Mapper(componentModel = "spring")
public abstract class FncMapper
{
    @Autowired protected RoleToFunctionAssRepo rtfRepo;
    @Autowired protected PrivilegeMapper prvMapper;
    @Autowired protected RoleMapper roleMapper;
    @Autowired protected IMenuReaderService menuService;
    @Autowired protected UserRepo userRepo;
    @Autowired protected PrvRepo prvRepo;

    @Mapping(target = "user", expression = "java(new rigeldevsolutions.gestasso.authmodule.model.entities.AppUser(dto.getUserId()))")
    @Mapping(target = "fncStatus", expression = "java(2)")
    @Mapping(target = "association", expression = "java(dto.getAssoId() == null ? null : new rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association(dto.getAssoId()))")
    @Mapping(target = "section", expression = "java(dto.getSectionId() == null ? null : new rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section(dto.getSectionId()))")
    @Mapping(target = "type", expression = "java(dto.getTypeCode() == null ? null : new rigeldevsolutions.gestasso.typemodule.model.entities.Type(dto.getTypeCode()))")
    public abstract AppFunction mapToFunction(CreateFncDTO dto);

    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "privileges", expression = "java(prvRepo.getFunctionPrvs(fnc.getId()))")
    @Mapping(target = "roles", expression = "java(rtfRepo.getFncRoles(fnc.getId()).stream().map(roleMapper::mapToReadRoleDTO).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "menus", expression = "java(menuService.getMenusByFncId(fnc.getId()))")
    public abstract ReadFncDTO mapToReadFncDto(AppFunction fnc);
}
