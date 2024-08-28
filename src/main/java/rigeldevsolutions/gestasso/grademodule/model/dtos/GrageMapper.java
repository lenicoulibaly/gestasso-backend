package rigeldevsolutions.gestasso.grademodule.model.dtos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rigeldevsolutions.gestasso.grademodule.model.entities.Grade;

@Mapper(componentModel = "spring")
public interface GrageMapper {

    @Mapping(target = "categorie", expression = "java(grade.getCategorie() == null ? null : grade.getCategorie().name())")
    ReadGradeDTO mapToReadGradeDTO(Grade grade);

    @Mapping(target = "categorie", expression = "java(org.apache.commons.lang3.EnumUtils.getEnum(rigeldevsolutions.gestasso.grademodule.model.enums.Categorie.class, dto.getCategorie()))")
    @Mapping(target = "nomGrade", expression = "java(dto.getCategorie() + dto.getRang())")
    Grade mapToGrade(CreateGradeDTO dto);
}