package rigeldevsolutions.gestasso.metier.cotisationmodule.controller.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.ReadAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.dtos.UpdateAssociationDTO;
import rigeldevsolutions.gestasso.metier.assomodule.model.entities.Association;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.CreateCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.ReadCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.UpdateCotisationDTO;

public interface ICotisationService
{
    ReadCotisationDTO createCotisation(CreateCotisationDTO dto, ActionIdentifier ai);

    ReadCotisationDTO updateCotisation(UpdateCotisationDTO dto, ActionIdentifier ai);

    Page<ReadCotisationDTO> searchCotisations(String key, Long assoId, Long sectionId, boolean actuel, Pageable pageable);
}
