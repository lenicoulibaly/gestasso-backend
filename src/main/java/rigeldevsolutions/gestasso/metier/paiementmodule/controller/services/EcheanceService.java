package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.cotisationmodule.controller.repositories.CotisationRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.EcheanceRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.PaiementRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.CreateEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.ReadEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.UpdateEcheanceDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeance;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeancier;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.mappers.EcheanceMapper;
import rigeldevsolutions.gestasso.metier.prelevementmodule.controller.repositories.PrelevementRepo;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.DateParser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;

@Service @RequiredArgsConstructor
public class EcheanceService implements IEcheanceService
{
    private final EcheanceMapper echeanceMapper;
    private final EcheanceRepo echeanceRepo;
    private final PaiementRepo paiementRepo;
    private final ICalculPaiementService calculPaiementService;
    private final CotisationRepo cotisationRepo;
    private final PrelevementRepo prelRepo;


    @Override @Transactional
    public ReadEcheanceDTO createEcheance(CreateEcheanceDTO dto, ActionIdentifier ai)
    {
        Echeance echeance = echeanceMapper.mapToEcheance(dto);
        echeance.setNomEcheance(this.getNomEcheance(dto.getDateEcheance(), dto.getFrequenceUniqueCode()));
        echeance = echeanceRepo.save(echeance);
        BeanUtils.copyProperties(ai, echeance);
        return echeanceMapper.mapToReadEcheanceDTO(echeance);
    }

    @Override
    public ReadEcheanceDTO updateEcheance(UpdateEcheanceDTO dto, ActionIdentifier ai)
    {
        Echeance echeance = echeanceRepo.findById(dto.getEcheanceId()).orElseThrow(()->new AppException("Echéance introuvable"));
        echeance.setEcheancier(new Echeancier(dto.getEcheancierId()));
        BeanUtils.copyProperties(dto, echeance, "echeancier");
        BeanUtils.copyProperties(ai, echeance);
        return echeanceMapper.mapToReadEcheanceDTO(echeance);
    }

    @Override
    public List<ReadEcheanceDTO> getNextEcheancesToPay(long nbr, Long cotisationId, Long adhesionId)
    {
        if(nbr == 0) return Collections.singletonList(this.getCurrentEcheanceToPay(cotisationId, adhesionId));
        Page<ReadEcheanceDTO> echeancePage = paiementRepo.getNextEcheances(cotisationId, adhesionId, PageRequest.of(0, (int)nbr));
        List<ReadEcheanceDTO> echeances = echeancePage != null ? echeancePage.getContent() : Collections.emptyList();
        echeances = echeances.stream().peek(e->e.setMontantEcheance(calculPaiementService.calculateResteAPayer(cotisationId, adhesionId, e.getEcheanceId()))).collect(Collectors.toList());
        return echeances;
    }

    @Override
    public ReadEcheanceDTO getCurrentEcheanceToPay(Long cotisationId, Long adhesionId)
    {
        Page<ReadEcheanceDTO> echeancePage = paiementRepo.getNextEcheances(cotisationId, adhesionId, PageRequest.of(0, 1));
        List<ReadEcheanceDTO> echeances = echeancePage != null ? echeancePage.getContent() : Collections.emptyList();
        if(echeances.isEmpty()) return null;
        ReadEcheanceDTO dto = echeances.get(echeances.size()-1);

        dto.setMontantEcheance(calculPaiementService.calculateResteAPayer(cotisationId, adhesionId, dto.getEcheanceId()));
        return dto;
    }

    @Override
    public ReadEcheanceDTO getNextEcheanceToPayAfterPaying(Long cotisationId, Long adhesionId, BigDecimal montantVersement)
    {
        long nbrEcheances = calculPaiementService.calculateNbrEcheancesSoldeesParVersement(cotisationId, adhesionId, montantVersement);
        BigDecimal montantCotisation = cotisationRepo.getMontantCotisation(cotisationId).orElseThrow(()->new AppException("Le montant de la cotisation ne peut être nul"));
        List<ReadEcheanceDTO> echeances = this.getNextEcheancesToPay(nbrEcheances, cotisationId, adhesionId);
        BigDecimal totalMontantEcheances = echeances.stream()
                .map(ReadEcheanceDTO::getMontantEcheance)
                .filter(montant -> montant != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal montantPaiementDerniereEcheance = montantVersement.subtract(totalMontantEcheances);
        ReadEcheanceDTO lastEcheance = echeances.get(echeances.size()-1);
        if(montantPaiementDerniereEcheance.compareTo(ZERO)<0)
        {
            lastEcheance.setMontantEcheance(lastEcheance.getMontantEcheance().subtract(montantVersement));
            return lastEcheance;
        }

        ReadEcheanceDTO nextEcheance = this.getNextEcheance(lastEcheance.getEcheanceId());
        if(nextEcheance == null) return null;
        nextEcheance.setMontantEcheance(montantCotisation.subtract(montantPaiementDerniereEcheance));

        return nextEcheance;
    }

    @Override
    public ReadEcheanceDTO getNextEcheance(Long echeanceId)
    {
        return echeanceRepo.getNextEcheance(echeanceId);
    }

    @Override
    public ReadEcheanceDTO getLastEcheance(Long echeancierId)
    {
        return echeanceRepo.getLastEcheance(echeancierId);
    }


    @Override
    public List<ReadEcheanceDTO> getEcheancesRetard(Long cotisationId, Long adhesionId) {
        return null;
    }

    @Override
    public ReadEcheanceDTO getCurrentEcheanceToPay(Long cotisationId) {
        Echeance lastEcheance = prelRepo.getLastEcheancePreleve(cotisationId);
        if(lastEcheance == null ) return this.getFirstEcheanceByCotisationId(cotisationId);
        ReadEcheanceDTO currentEcheance = this.getNextEcheance(lastEcheance.getEcheanceId());
        if(currentEcheance == null) throw new AppException("Toutes échéances de la cotisation ont été prélevée");
        return currentEcheance;
    }

    @Override
    public ReadEcheanceDTO getFirstEcheanceByCotisationId(Long cotisationId)
    {
        ReadEcheanceDTO echeance = echeanceRepo.getFirstEcheanceByCotisationId(cotisationId);
        return echeance;
    }

    private String getNomEcheance(LocalDate date, String frenquenceUniqueCode)
    {
        if(date == null || frenquenceUniqueCode == null) return "";

        return switch (frenquenceUniqueCode) {

            case "HEBDOMADAIRE" -> DateParser.getWeekOfYear(date);
            case "MENSUEL" -> DateParser.getMonthAndYear(date);
            case "TRIMESTRIEL" -> DateParser.getTrimester(date);
            case "SEMESTRIEL" -> DateParser.getSemester(date);
            default -> String.valueOf(date.getYear());
        };
    }
}
