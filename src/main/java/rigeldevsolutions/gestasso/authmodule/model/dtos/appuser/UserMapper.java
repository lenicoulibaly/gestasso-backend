package rigeldevsolutions.gestasso.authmodule.model.dtos.appuser;

import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IFunctionService;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper
{
    @Autowired protected IFunctionService functionService;

    @Mapping(target="active", expression="java(false)")
    @Mapping(target="notBlocked", expression="java(true)")
    public abstract AppUser mapToUser(CreateUserDTO dto);

    @Mapping(target="active", expression="java(true)")
    @Mapping(target="notBlocked", expression="java(true)")
    public abstract AppUser mapToUser(CreateActiveUserDTO dto);

    @Mapping(target = "civilite", source = "civilite.name")
    @Mapping(target = "nationalite", source = "nationalite.nationalite")
    @Mapping(target = "typePiece", source = "typePiece.name")
    @Mapping(target = "codeTypePiece", source = "civilite.uniqueCode")
    @Mapping(target = "typeUtilisateur", source = "typeUtilisateur.name")
    @Mapping(target = "codeTypeUtilisateur", source = "typeUtilisateur.uniqueCode")
    @Mapping(target = "statut", source = "statut.staLibelle")
    @Mapping(target = "codeStatut", source = "statut.staCode")
    public abstract ReadUserDTO mapToReadUserDTO(AppUser user);

    @Mapping(target = "currentFunctionName", expression = "java(user.getUserId() == null ? null : functionService.getActiveCurrentFunctionName(user.getUserId()))")
    @Mapping(target = "statut", source = "statut.staLibelle")
    public abstract ListUserDTO mapToListUserDTO(AppUser user);
}