package rigeldevsolutions.gestasso.metier.cotisationmodule.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.CreateCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.ReadCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.entities.Cotisation;

@Mapper(componentModel = "spring")
public interface CotisationMapper
{
    @Mapping(target = "frequenceCotisation", expression = """
            java(dto.getFrequenceCotisation() == null ? null : 
            new rigeldevsolutions.gestasso.typemodule.model.entities.Type(dto.getFrequenceCotisation()))
            """)
    @Mapping(target = "modePrelevement", expression = """
            java(dto.getModePrelevement() == null ? null : 
            new rigeldevsolutions.gestasso.typemodule.model.entities.Type(dto.getModePrelevement()))
            """)
    @Mapping(target = "association", expression = """
            java(dto.getAssoId() == null ? null : 
            new rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association(dto.getAssoId()))
            """)
    @Mapping(target = "section", expression = """
            java(dto.getSectionId() == null ? null : 
            new rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section(dto.getSectionId()))
            """)
    Cotisation mapToCotisation(CreateCotisationDTO dto);



    @Mapping(target = "frequenceCotisation", source = "frequenceCotisation.name")
    @Mapping(target = "modePrelevement", source = "modePrelevement.name")
    @Mapping(target = "frequenceCotisationCode", source = "frequenceCotisation.uniqueCode")
    @Mapping(target = "modePrelevementCode", source = "modePrelevement.uniqueCode")
    @Mapping(target = "assoName", source = "association.assoName")
    @Mapping(target = "assoSigle", source = "association.sigle")
    @Mapping(target = "assoId", source = "association.assoId")
    @Mapping(target = "sectionName", source = "section.sectionName")
    @Mapping(target = "sectionSigle", source = "section.sigle")
    @Mapping(target = "sectionId", source = "section.sectionId")
    ReadCotisationDTO mapToReadCotisationDTO(Cotisation cotisation);
    //TODO Ne pas oublier le mapping des champ de montant lorsque le module paiement sera prêt

}