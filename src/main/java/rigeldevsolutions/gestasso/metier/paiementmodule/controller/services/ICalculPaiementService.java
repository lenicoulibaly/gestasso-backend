package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import java.math.BigDecimal;

public interface ICalculPaiementService
{
    BigDecimal calculateDejaPaye(Long cotisationId, Long adhesionId);
    BigDecimal calculateResteAPayer(Long cotisationId, Long adhesionId);
    BigDecimal calculateResteAPayer(Long cotisationId, Long adhesionId, Long echeanceId);
    BigDecimal calculateMontantRetard(Long cotisationId, Long adhesionId);
    BigDecimal calculateMontantAttenduParMembreOnCotisation(Long cotisationId);

    BigDecimal calculateDejaPaye(Long cotisationId, Long adhesionId, Long echeanceId);

    long calculateNbrEcheancesSoldeesParVersement(Long cotisationId, Long adhesionId, BigDecimal montantVersement);

    long calculateNbrEcheancesForCotisation(Long cotisationId);

    BigDecimal calculateMontantVersementSouhaite(Long cotisationId, Long adhesionId);
}
