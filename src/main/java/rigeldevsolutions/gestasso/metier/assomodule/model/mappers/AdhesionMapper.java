package rigeldevsolutions.gestasso.metier.assomodule.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateAdhesionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion;

@Mapper(componentModel = "spring")
public interface AdhesionMapper
{
    @Mapping(target = "association", expression = "java(dto.getAssoId() == null ? null : new rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association(dto.getAssoId()))")
    @Mapping(target = "section", expression = "java(dto.getSectionId() == null ? null : new rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section(dto.getSectionId()))")
    @Mapping(target = "member", expression = "java(dto.getUserId() == null ? null : new rigeldevsolutions.gestasso.authmodule.model.entities.AppUser(dto.getUserId()))")
    @Mapping(target = "active", expression = "java(true)")
    Adhesion mapToAdhesion(CreateAdhesionDTO dto);
}
