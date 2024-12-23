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
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.DateParser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;

@Service @RequiredArgsConstructor
public class EcheanceService implements IEcheanceService
{
    private final EcheanceMapper echeanceMapper;
    private final EcheanceRepo echeanceRepo;
    private final PaiementRepo paiementRepo;
    private final CotisationRepo cotisationRepo;

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
    public List<ReadEcheanceDTO> getNextEcheancesToPay(int nbr, Long cotisationId, Long adhesionId)
    {
        Page<ReadEcheanceDTO> echeancePage = paiementRepo.getNextEcheances(cotisationId, adhesionId, PageRequest.of(0, nbr));
        List<ReadEcheanceDTO> echeances = echeancePage != null ? echeancePage.getContent() : Collections.emptyList();
        echeances = echeances.stream().peek(e->e.setMontantEcheance(this.calculateResteAPayer(cotisationId, adhesionId, e.getEcheanceId()))).collect(Collectors.toList());
        return echeances;
    }

    @Override
    public ReadEcheanceDTO getNextEcheance(Long echeanceId)
    {
        return echeanceRepo.getNextEcheance(echeanceId);
    }


    @Override
    public ReadEcheanceDTO getEcheancesRetard(Long cotisationId, Long adhesionId) {
        return null;
    }

    @Override
    public BigDecimal calculateResteAPayer(Long cotisationId, Long adhesionId, Long echeanceId)
    {
        BigDecimal dejaPaye = this.calculateDejaPaye(cotisationId, adhesionId, echeanceId);
        BigDecimal montantCotisation = Optional.of(cotisationRepo.getMotantCotisation(cotisationId)).orElse(ZERO);
        BigDecimal resteAPayer = montantCotisation.subtract(dejaPaye);
        return resteAPayer.compareTo(ZERO)<0 ? ZERO : resteAPayer ;
    }

    @Override
    public BigDecimal calculateDejaPaye(Long cotisationId, Long adhesionId, Long echeanceId)
    {
        BigDecimal dejaPaye = Optional.ofNullable(paiementRepo.calculateDejaPaye(cotisationId, adhesionId, echeanceId)).orElse(ZERO);
        return dejaPaye;
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
