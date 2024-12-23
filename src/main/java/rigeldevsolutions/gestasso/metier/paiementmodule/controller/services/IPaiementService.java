package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import org.springframework.data.domain.Page;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.PaiementDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.VersementDTO;

import java.math.BigDecimal;

public interface IPaiementService {
    VersementDTO createVersementCotisation(PaiementDTO dto, ActionIdentifier ai);
    Page<PaiementDTO> findPaiementByCotisation(Long cotisationId);
    BigDecimal calculateResteAPayer(Long cotisationId, Long adhesionId);
}
