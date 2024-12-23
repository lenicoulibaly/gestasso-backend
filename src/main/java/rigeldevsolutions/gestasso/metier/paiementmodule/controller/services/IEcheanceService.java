package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.CreateEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.UpdateEcheanceDTO;

import java.math.BigDecimal;
import java.util.List;

public interface IEcheanceService
{
    ReadEcheanceDTO createEcheance(CreateEcheanceDTO dto, ActionIdentifier ai);
    ReadEcheanceDTO updateEcheance(UpdateEcheanceDTO dto, ActionIdentifier ai);
    List<ReadEcheanceDTO> getNextEcheancesToPay(int nbr, Long cotisationId, Long adhesionId);
    ReadEcheanceDTO getNextEcheance(Long echeanceId);
    ReadEcheanceDTO getEcheancesRetard(Long cotisationId, Long adhesionId);
    BigDecimal calculateResteAPayer(Long cotisationId, Long adhesionId, Long echeanceId);
    BigDecimal calculateDejaPaye(Long cotisationId, Long adhesionId, Long echeanceId);
}
