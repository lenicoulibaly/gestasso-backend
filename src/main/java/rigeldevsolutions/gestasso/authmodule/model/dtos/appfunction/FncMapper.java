package rigeldevsolutions.gestasso.authmodule.model.dtos.appfunction;

import rigeldevsolutions.gestasso.authmodule.controller.repositories.PrvRepo;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.RoleToFunctionAssRepo;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.UserRepo;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IMenuReaderService;
import rigeldevsolutions.gestasso.authmodule.model.dtos.appprivilege.PrivilegeMapper;
import rigeldevsolutions.gestasso.authmodule.model.dtos.approle.RoleMapper;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppFunction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

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
    public abstract AppFunction mapToFunction(CreateFncDTO dto);

    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "privileges", expression = "java(prvRepo.getFunctionPrvs(fnc.getId()))")
    @Mapping(target = "roles", expression = "java(rtfRepo.getFncRoles(fnc.getId()).stream().map(roleMapper::mapToReadRoleDTO).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "menus", expression = "java(menuService.getMenusByFncId(fnc.getId()))")
    public abstract ReadFncDTO mapToReadFncDto(AppFunction fnc);
}
