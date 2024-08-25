package rigeldevsolutions.gestasso.metier.paiementmodule.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.EcheancierRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.CreateEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO2;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeance;

@Mapper(componentModel = "spring")
public abstract class EcheanceMapper
{
    @Autowired protected EcheancierRepo echeancierRepo;
    @Mapping(target = "echeancier", expression = "java(dto.getEcheancierId() == null ? null : new rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeancier(dto.getEcheancierId()))")
    public abstract Echeance mapToEcheance(CreateEcheanceDTO dto);

    @Mapping(target = "echeancierId", source = "echeancier.echeancierId")
    @Mapping(target = "echeancierTypeCode", source = "echeancier.typeEcheancier.uniqueCode")
    @Mapping(target = "echeancierTypeName", source = "echeancier.typeEcheancier.name")
    @Mapping(target = "echeancierFrequence", expression = "java(e.getEcheancier() == null || e.getEcheancier().getEcheancierId() == null ? null : echeancierRepo.findById(e.getEcheancier().getEcheancierId()).orElseThrow(()->new rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException(\"Echéancier introuvable\")).getFrequence().name())")
    @Mapping(target = "echeancierName", source = "echeancier.name")
    @Mapping(target = "exeCode", source = "echeancier.exercice.exeCode")
    public abstract ReadEcheanceDTO mapToReadEcheanceDTO(Echeance e);

    public abstract ReadEcheanceDTO2 mapToReadEcheanceDTO2(Echeance e);
    public abstract ReadEcheanceDTO2 mapToReadEcheanceDTO2(ReadEcheanceDTO e);
}