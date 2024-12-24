package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.repositories.CotisationRepo;
import rigeldevsolutions.gestasso.metier.cotisationmodule.model.entities.Cotisation;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.EcheanceRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.PaiementRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO;
import rigeldevsolutions.gestasso.metier.views.EcheanceCotisationRepo;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.MontantFormater;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;

@Service
public class CalculPaiementService implements ICalculPaiementService
{
    private final CotisationRepo cotisationRepo;
    private final EcheanceRepo echeanceRepo;
    private final PaiementRepo paiementRepo;
    private final EcheanceCotisationRepo echeanceCotisationRepo;
    private final @Lazy IEcheanceService echeanceService;

    public CalculPaiementService(CotisationRepo cotisationRepo, EcheanceRepo echeanceRepo,
                                 PaiementRepo paiementRepo,
                                 EcheanceCotisationRepo echeanceCotisationRepo,
                                 @Lazy IEcheanceService echeanceService) {
        this.cotisationRepo = cotisationRepo;
        this.echeanceRepo = echeanceRepo;
        this.paiementRepo = paiementRepo;
        this.echeanceCotisationRepo = echeanceCotisationRepo;
        this.echeanceService = echeanceService;
    }

    @Override
    public BigDecimal calculateDejaPaye(Long cotisationId, Long adhesionId)
    {
        BigDecimal dejaPaye = Optional.ofNullable(paiementRepo.calculateDejaPaye(cotisationId, adhesionId)).orElse(ZERO);
        return dejaPaye;
    }

    @Override
    public BigDecimal calculateResteAPayer(Long cotisationId, Long adhesionId)
    {
        BigDecimal dejaPaye = this.calculateDejaPaye(cotisationId, adhesionId);
        BigDecimal montantAttenduParMembre = this.calculateMontantAttenduParMembreOnCotisation(cotisationId);
        return montantAttenduParMembre.subtract(dejaPaye);
    }

    @Override
    public BigDecimal calculateMontantRetard(Long cotisationId, Long adhesionId)
    {
        BigDecimal montantTotalAttenduATerme = echeanceCotisationRepo.calculateMontantStrictAttenduATerme(cotisationId, LocalDate.now());
        BigDecimal dejaPaye = this.calculateDejaPaye(cotisationId, adhesionId);
        if(montantTotalAttenduATerme == null || montantTotalAttenduATerme.compareTo(ZERO) == 0 || montantTotalAttenduATerme.compareTo(dejaPaye)<0) return ZERO;
        return montantTotalAttenduATerme.subtract(dejaPaye);
    }

    @Override
    public BigDecimal calculateMontantVersementSouhaite(Long cotisationId, Long adhesionId) {
        Long delaisRigueur = cotisationRepo.getDelaiRigueur(cotisationId);
        BigDecimal montantTotalAttenduATerme = echeanceCotisationRepo.calculateMontantStrictAttenduATerme(cotisationId, LocalDate.now().plusDays(delaisRigueur));
        BigDecimal dejaPaye = this.calculateDejaPaye(cotisationId, adhesionId);
        if(montantTotalAttenduATerme == null || montantTotalAttenduATerme.compareTo(ZERO) == 0 || montantTotalAttenduATerme.compareTo(dejaPaye)<0) return ZERO;
        return montantTotalAttenduATerme.subtract(dejaPaye);
    }

    @Override
    public BigDecimal calculateMontantAttenduParMembreOnCotisation(Long cotisationId)
    {
        Cotisation cotisation = cotisationRepo.findById(cotisationId).orElseThrow(()->new AppException("Cotisation introuvable"));
        BigDecimal montantCotisation = Optional.ofNullable(cotisation.getMontantCotisation()).orElse(ZERO);
        Long nbrEcheances = this.calculateNbrEcheancesForCotisation(cotisationId);
        return montantCotisation.multiply(new BigDecimal(nbrEcheances));
    }

    @Override
    public BigDecimal calculateResteAPayer(Long cotisationId, Long adhesionId, Long echeanceId)
    {
        BigDecimal dejaPaye = this.calculateDejaPaye(cotisationId, adhesionId, echeanceId);
        BigDecimal montantCotisation = cotisationRepo.getMontantCotisation(cotisationId).orElse(ZERO);
        BigDecimal resteAPayer = montantCotisation.subtract(dejaPaye);
        return resteAPayer.compareTo(ZERO)<0 ? ZERO : resteAPayer ;
    }

    @Override
    public BigDecimal calculateDejaPaye(Long cotisationId, Long adhesionId, Long echeanceId)
    {
        BigDecimal dejaPaye = Optional.ofNullable(paiementRepo.calculateDejaPaye(cotisationId, adhesionId, echeanceId)).orElse(ZERO);
        return dejaPaye;
    }

    @Override
    public long calculateNbrEcheancesSoldeesParVersement(Long cotisationId, Long adhesionId, BigDecimal montantVersement)
    {
        if(montantVersement == null || montantVersement.compareTo(ZERO) == 0) return 0L;
        if(cotisationId == null) throw new AppException("Veuillez fournir l'ID de la cotisation");
        if(adhesionId == null) throw new AppException("Veuillez fournir l'ID de l'adhésion");
        //Cotisation cotisation = cotisationRepo.findById(cotisationId).orElseThrow(()->new AppException("Cotisation introuvable " + cotisationId));
        BigDecimal restaAPayer = this.calculateResteAPayer(cotisationId, adhesionId);
        if(montantVersement.compareTo(restaAPayer) > 0 ) throw new AppException("Le montant du versement ne peut excéder le reste à payer (" + MontantFormater.format(restaAPayer) + " F CFA)");
        BigDecimal montantCotisation = cotisationRepo.getMontantCotisation(cotisationId).orElseThrow(()->new AppException("Le montant de la cotisation ne peut être nul"));
        if(montantCotisation == null || montantCotisation.compareTo(ZERO) == 0) throw new AppException("Le montant de la cotisation est nul");
        long nbrEcheances = montantVersement.divideToIntegralValue(montantCotisation).longValue();

        List<ReadEcheanceDTO> echeances = echeanceService.getNextEcheancesToPay(nbrEcheances+1, cotisationId, adhesionId);
        if(echeances == null || echeances.isEmpty()) return 0;
        BigDecimal totalMontantEcheances = echeances.stream()
                .map(ReadEcheanceDTO::getMontantEcheance)
                .filter(montant -> montant != null) // Éviter les nulls
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if(montantVersement.compareTo(totalMontantEcheances) >= 0) return nbrEcheances+1;
        else return nbrEcheances;
    }

    @Override
    public long calculateNbrEcheancesForCotisation(Long cotisationId)
    {
        Cotisation cotisation = cotisationRepo.findById(cotisationId).orElseThrow(()->new AppException("Cotisation introuvable"));
        LocalDate dateFin = cotisation.getDateFinCotisation();
        if(dateFin == null)
        {
            String typeFrequence = cotisation.getFrequenceCotisation().getUniqueCode();
            ReadEcheanceDTO lastEcheance = echeanceRepo.getLastEcheance(typeFrequence);
            dateFin = lastEcheance.getDateEcheance();
        }
        LocalDate dateDebut = cotisation.getDateDebutCotisation();

        if(dateDebut == null) dateDebut = cotisation.getCreatedAt().toLocalDate();
        return switch (cotisation.getFrequenceCotisation().getUniqueCode())
                {
                    case "JOURNALIERE" -> ChronoUnit.DAYS.between(dateDebut, dateFin);
                    case "HEBDOMADAIRE" -> ChronoUnit.WEEKS.between(dateDebut, dateFin);
                    case "MENSUEL" -> ChronoUnit.MONTHS.between(dateDebut, dateFin);
                    case "TRIMESTRIEL" -> ChronoUnit.MONTHS.between(dateDebut, dateFin)/3;
                    case "SEMESTRIEL" -> ChronoUnit.MONTHS.between(dateDebut, dateFin)/6;
                    default -> ChronoUnit.YEARS.between(dateDebut, dateFin);
                };
    }
}
