package rigeldevsolutions.gestasso.metier.assomodule.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Section;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.entities.Cotisation;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.mappers.CotisationMapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AssoMapper
{
    @Autowired protected CotisationMapper cotisationMapper;
    public abstract Association mapToAssociation(CreateAssociationDTO dto);
}