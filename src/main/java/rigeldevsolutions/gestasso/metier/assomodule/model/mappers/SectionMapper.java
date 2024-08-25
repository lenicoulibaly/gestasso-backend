package rigeldevsolutions.gestasso.metier.assomodule.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateSectionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadSectionDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section;

@Mapper(componentModel = "spring")
public interface SectionMapper
{
    @Mapping(target = "association", expression = "java(dto.getAssoId() == null ? null : new rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association(dto.getAssoId()))")
    @Mapping(target = "strTutelle", expression = "java(dto.getStrId() == null ? null : new rigeldevsolutions.gestasso.structuremodule.model.entities.Structure(dto.getStrId()))")
    Section mapToSection(CreateSectionDTO dto);

    @Mapping(target = "assoId", source = "association.assoId")
    @Mapping(target = "assoName", source = "association.assoName")
    @Mapping(target = "strName", source = "strTutelle.strName")
    @Mapping(target = "strSigle", source = "strTutelle.strSigle")
    ReadSectionDTO mapToReadSectionDTO(Section section);
}