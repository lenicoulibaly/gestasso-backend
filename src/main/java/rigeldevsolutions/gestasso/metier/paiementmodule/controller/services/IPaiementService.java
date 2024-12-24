package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import org.springframework.data.domain.Page;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.PaiementCotisationDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.VersementDTO;

public interface IPaiementService {
    VersementDTO createVersementCotisation(PaiementCotisationDTO dto, ActionIdentifier ai);
    Page<PaiementCotisationDTO> findPaiementsByCotisation(Long cotisationId);


    PaiementCotisationDTO getPaiementCotisationDto(PaiementCotisationDTO dto);
}
