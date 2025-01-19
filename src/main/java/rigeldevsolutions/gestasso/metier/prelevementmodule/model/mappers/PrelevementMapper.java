package rigeldevsolutions.gestasso.metier.prelevementmodule.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos.DefautPrelevementDTO;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.dtos.PrelevementDTO;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.entities.DefautPrelevement;
import rigeldevsolutions.gestasso.metier.prelevementmodule.model.entities.Prelevement;

@Mapper(componentModel = "spring")
public interface PrelevementMapper
{
    @Mapping(target = "echeance", expression = "java(dto.getEcheanceId() == null ? null : new rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeance(dto.getEcheanceId()))")
    @Mapping(target = "cotisation", expression = "java(dto.getCotisationId() == null ? null : new rigeldevsolutions.gestasso.metier.cotisationmodule.model.entities.Cotisation(dto.getCotisationId()))")
    Prelevement mapToPrelevementCotisation(PrelevementDTO dto);

    @Mapping(target = "echeanceId", source = "prelevement.echeance.echeanceId")
    @Mapping(target = "nomEcheance", source = "prelevement.echeance.nomEcheance")
    @Mapping(target = "cotisationId", source = "prelevement.cotisation.cotisationId")
    @Mapping(target = "nomCotisation", source = "prelevement.cotisation.nomCotisation")
    @Mapping(target = "motif", source = "prelevement.cotisation.motif")
    PrelevementDTO mapToPrelevementDTO(Prelevement prelevement);

    @Mapping(target = "adhesion", expression = "java(dto.getAdhesionId() == null ? null : new rigeldevsolutions.gestasso.metier.assomodule.model.entities.Adhesion(dto.getAdhesionId()))")
    @Mapping(target = "prelevement", expression = "java(dto.getPrelevementId() == null ? null : new rigeldevsolutions.gestasso.metier.prelevementmodule.model.entities.Prelevement(dto.getPrelevementId()))")
    @Mapping(target = "motif", source = "dto.motifDefaut")
    DefautPrelevement mapToDefautPrelevement(DefautPrelevementDTO dto);
}