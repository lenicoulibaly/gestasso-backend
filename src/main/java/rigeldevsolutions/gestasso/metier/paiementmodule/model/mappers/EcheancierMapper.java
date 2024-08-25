package rigeldevsolutions.gestasso.metier.paiementmodule.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.CreateEcheancierDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEchancierDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeancier;

@Mapper(componentModel = "spring")
public interface EcheancierMapper
{
    @Mapping(target = "typeEcheancier", expression = "java(dto.getTypeEcheancierCode() == null ? null : new rigeldevsolutions.gestasso.typemodule.model.entities.Type(dto.getTypeEcheancierCode()))")
    @Mapping(target = "frequence", expression = """
            java(org.apache.commons.lang3.EnumUtils.isValidEnum(rigeldevsolutions.gestasso.metier.paiementmodule.model.enums.Frequence.class, dto.getFrequence()) ? 
            org.apache.commons.lang3.EnumUtils.getEnum(rigeldevsolutions.gestasso.metier.paiementmodule.model.enums.Frequence.class, dto.getFrequence()) :
            null)""")
    @Mapping(target = "exercice", expression = "java(dto.getExeCode() == null ? null : new rigeldevsolutions.gestasso.metier.exercicemodule.model.entities.Exercice(dto.getExeCode()))")
    Echeancier mapToEcheancier(CreateEcheancierDTO dto);

    @Mapping(target = "typeEcheancierCode", source = "typeEcheancier.uniqueCode")
    @Mapping(target = "typeEcheancierName", source = "typeEcheancier.name")
    @Mapping(target = "frequence", expression = "java(echeancier.getFrequence().name())")
    @Mapping(target = "exeCode", source = "exercice.exeCode")
    ReadEchancierDTO mapToReadEcheancierDTO(Echeancier echeancier);
}