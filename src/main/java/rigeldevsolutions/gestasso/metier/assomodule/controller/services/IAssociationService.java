package rigeldevsolutions.gestasso.metier.assomodule.controller.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.CreateAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.UpdateAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;

public interface IAssociationService
{
    Association createAssociation(CreateAssociationDTO dto, ActionIdentifier ai);
    Association updateAssociation(UpdateAssociationDTO dto, ActionIdentifier ai);
    Page<ReadAssociationDTO> searchAssociations(String key, Pageable pageable);
}
