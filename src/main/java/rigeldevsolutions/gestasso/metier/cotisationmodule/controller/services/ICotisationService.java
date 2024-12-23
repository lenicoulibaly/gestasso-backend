package rigeldevsolutions.gestasso.metier.cotisationmodule.controller.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.CreateCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.ReadCotisationDTO;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.dtos.UpdateCotisationDTO;

import java.math.BigDecimal;

public interface ICotisationService
{
    ReadCotisationDTO createCotisation(CreateCotisationDTO dto, ActionIdentifier ai);

    ReadCotisationDTO updateCotisation(UpdateCotisationDTO dto, ActionIdentifier ai);

    Page<ReadCotisationDTO> searchCotisations(String key, Long assoId, Long sectionId, boolean actuel, Pageable pageable);

    long determineNbrEcheances(Long cotisationId);
    BigDecimal calculateMontantAttenduParMembre(Long cotisationId);
}
