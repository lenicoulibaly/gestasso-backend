package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import java.math.BigDecimal;

public interface ICalculEcheanceService
{
    BigDecimal calculateResteAPayer(Long cotisationId, Long adhesionId, Long echeanceId);
    BigDecimal calculateDejaPaye(Long cotisationId, Long adhesionId, Long echeanceId);

    long calculateNbrEcheancesSoldeesParVersement(Long cotisationId, Long adhesionId, BigDecimal montantVersement);

}
