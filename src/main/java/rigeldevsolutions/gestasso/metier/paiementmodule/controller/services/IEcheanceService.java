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
    List<ReadEcheanceDTO> getNextEcheancesToPay(long nbr, Long cotisationId, Long adhesionId);
    ReadEcheanceDTO getCurrentEcheanceToPay(Long cotisationId, Long adhesionId);
    ReadEcheanceDTO getNextEcheanceToPayAfterPaying(Long cotisationId, Long adhesionId, BigDecimal montant);

    ReadEcheanceDTO getNextEcheance(Long echeanceId);
    ReadEcheanceDTO getLastEcheance(Long echeancierId);
    List<ReadEcheanceDTO> getEcheancesRetard(Long cotisationId, Long adhesionId);

    ReadEcheanceDTO getCurrentEcheanceToPay(Long cotisationId);

    ReadEcheanceDTO getFirstEcheanceByCotisationId(Long cotisationId);
}
