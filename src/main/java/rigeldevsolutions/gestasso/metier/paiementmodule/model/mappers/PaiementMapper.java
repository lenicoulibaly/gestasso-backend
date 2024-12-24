package rigeldevsolutions.gestasso.metier.paiementmodule.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.PaiementCotisationDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.VersementDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Paiement;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Versement;

@Mapper(componentModel = "spring")
public interface PaiementMapper
{

    Paiement mapToPaiementCotisation(PaiementCotisationDTO dto);

    @Mapping(target = "datePaiement", source = "versement.dateVersement")
    @Mapping(target = "modePaiementCode", source = "versement.modePaiement.uniqueCode")
    @Mapping(target = "modePaiement", source = "versement.modePaiement.name")
    @Mapping(target = "typePaiementCode", source = "versement.typePaiement.uniqueCode")
    @Mapping(target = "typePaiement", source = "versement.typePaiement.name")
    @Mapping(target = "adhesionId", source = "versement.adhesion.adhesionId")

    @Mapping(target = "firstName", source = "versement.adhesion.member.firstName")
    @Mapping(target = "lastName", source = "versement.adhesion.member.lastName")
    @Mapping(target = "email", source = "versement.adhesion.member.email")

    @Mapping(target = "cotisationId", source = "versement.cotisation.cotisationId")
    @Mapping(target = "nomCotisation", source = "versement.cotisation.nomCotisation")
    @Mapping(target = "motif", source = "versement.cotisation.motif")
    PaiementCotisationDTO mapToPaiementDTO(Paiement paiement);

    @Mapping(target = "dateVersement", source = "datePaiement")
    @Mapping(target = "modePaiement", expression = "java(new rigeldevsolutions.gestasso.typemodule.model.entities.Type(dto.getModePaiementCode()))")
    @Mapping(target = "typePaiement", expression = "java(new rigeldevsolutions.gestasso.typemodule.model.entities.Type(dto.getTypePaiementCode()))")
    @Mapping(target = "adhesion", expression = "java(dto.getAdhesionId() == null ? null : new rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion(dto.getAdhesionId()))")
    @Mapping(target = "cotisation", expression = "java(dto.getCotisationId() == null ? null : new rigeldevsolutions.gestasso.metier.cotisationmodule.model.entities.Cotisation(dto.getCotisationId()))")
    Versement mapToVersement(PaiementCotisationDTO dto);

    @Mapping(target = "modePaiement", source = "modePaiement.name")
    @Mapping(target = "modePaiementCode", source = "modePaiement.uniqueCode")
    @Mapping(target = "typePaiement", source = "typePaiement.name")
    @Mapping(target = "typePaiementCode", source = "typePaiement.uniqueCode")
    @Mapping(target = "adhesionId", source = "adhesion.adhesionId")
    @Mapping(target = "firstName", source = "adhesion.member.firstName")
    @Mapping(target = "lastName", source = "adhesion.member.lastName")
    @Mapping(target = "email", source = "adhesion.member.email")
    @Mapping(target = "cotisationId", source = "cotisation.cotisationId")
    @Mapping(target = "nomCotisation", source = "cotisation.nomCotisation")
    @Mapping(target = "motif", source = "cotisation.motif")
    VersementDTO mapToVersementDto(Versement versement);
}
/*
modePaiementCode;
    private String modePaiement;
    private String typePaiementCode;
    private String typePaiement;
 */