package rigeldevsolutions.gestasso.metier.paiementmodule.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.CreateEcheancierDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheancierDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeancier;

@Mapper(componentModel = "spring")
public interface EcheancierMapper
{
    @Mapping(target = "typeEcheancier", expression = "java(dto.getTypeEcheancierCode() == null ? null : new rigeldevsolutions.gestasso.typemodule.model.entities.Type(dto.getTypeEcheancierCode()))")
    @Mapping(target = "frequence", expression = "java(dto.getFrequenceTypeCode() == null ? null : new rigeldevsolutions.gestasso.typemodule.model.entities.Type(dto.getFrequenceTypeCode()))")
    @Mapping(target = "exercice", expression = "java(dto.getExeCode() == null ? null : new rigeldevsolutions.gestasso.metier.exercicemodule.model.entities.Exercice(dto.getExeCode()))")
    Echeancier mapToEcheancier(CreateEcheancierDTO dto);

    @Mapping(target = "typeEcheancierCode", source = "typeEcheancier.uniqueCode")
    @Mapping(target = "typeEcheancierName", source = "typeEcheancier.name")
    @Mapping(target = "frequenceTypeCode", source = "frequence.uniqueCode")
    @Mapping(target = "frequenceTypeName", source = "frequence.name")
    @Mapping(target = "exeCode", source = "exercice.exeCode")
    ReadEcheancierDTO mapToReadEcheancierDTO(Echeancier echeancier);
}