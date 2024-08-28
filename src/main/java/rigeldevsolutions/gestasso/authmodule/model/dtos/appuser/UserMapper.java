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
    public abstract ReadUserDTO mapToReadUserDTO(AppUser user);

    @Mapping(target = "civilite", expression = "java(dto.getCodeCivilite() == null ? null : new rigeldevsolutions.gestasso.typemodule.model.entities.Type(dto.getCodeCivilite()))")
    @Mapping(target = "nationalite", expression = "java(dto.getCodePays() == null ? null : new rigeldevsolutions.gestasso.authmodule.model.entities.Nationalite(dto.getCodePays()))")
    @Mapping(target = "grade", expression = "java(dto.getGradeCode() == null ? null : new rigeldevsolutions.gestasso.grademodule.model.entities.Grade(dto.getGradeCode()))")
    @Mapping(target = "active", expression = "java(false)")
    @Mapping(target = "notBlocked", expression = "java(true)")
    public abstract AppUser mapToAdherant(CreateAdherantDTO dto);
}