package rigeldevsolutions.gestasso.typemodule.model.dtos;

import rigeldevsolutions.gestasso.typemodule.model.entities.Type;
import rigeldevsolutions.gestasso.typemodule.model.entities.TypeParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TypeMapper
{
    @Mapping(target = "status", expression = "java(rigeldevsolutions.gestasso.sharedmodule.enums.PersStatus.ACTIVE)")
    @Mapping(target = "typeGroup", expression = "java(rigeldevsolutions.gestasso.typemodule.model.enums.TypeGroup.valueOf(dto.getTypeGroup()))")
    @Mapping(target = "uniqueCode", expression = "java(dto.getUniqueCode().toUpperCase())")
    @Mapping(target = "name", expression = "java(dto.getName().toUpperCase())")
    Type mapToType(CreateTypeDTO dto);

    Type mapToType(UpdateTypeDTO dto);

    @Mapping(target = "typeGroup", expression = "java(type.getTypeGroup().getGroupName())")
    ReadTypeDTO mapToReadTypeDTO(Type type);
    @Mapping(target = "parent.uniqueCode", source = "dto.parentCode")
    @Mapping(target = "child.uniqueCode", source = "dto.childCode")
    TypeParam mapToTypeParam(TypeParamDTO dto);
}