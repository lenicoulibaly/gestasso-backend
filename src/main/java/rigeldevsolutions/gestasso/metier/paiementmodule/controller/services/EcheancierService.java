package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.exercicemodule.controller.repositories.ExerciceRepo;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.entities.Exercice;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.EcheanceRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.EcheancierRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.*;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeancier;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.mappers.EcheanceMapper;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.mappers.EcheancierMapper;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service @RequiredArgsConstructor
public class EcheancierService implements IEcheancierService
{
    private final EcheancierRepo echeancierRepo;
    private final EcheanceRepo echeanceRepo;
    private final EcheancierMapper echeancierMapper;
    private final EcheanceMapper echeanceMapper;
    private final IEcheanceService echeanceService;
    private final ExerciceRepo exeRepo;
    //@Override
    private List<LocalDate> getDatesOfEcheancier(String frequenceTypeCode, int annee)
    {
        return switch (frequenceTypeCode) {
            case "JOURNALIERE" -> this.getDatesOfEcheancierJournalier(annee);
            case "HEBDOMADAIRE" -> this.getDatesOfEcheancierHebdomadaire(annee);
            case "MENSUEL" -> this.getDatesOfEcheancierMensuel(annee);
            case "TRIMESTRIEL" -> this.getDatesOfEcheancierTrimestriel(annee);
            case "SEMESTRIEL" -> this.getDatesOfEcheancierSemestriel(annee);
            default -> this.getDatesOfEcheancierAnnuel(annee);
        };
    }

    private String getNomEcheancier(String frequenceTypeCode, int exeCode)
    {
        return switch (frequenceTypeCode)
        {
            case "JOURNALIERE" -> "Echéancier journalier naturel " + exeCode;
            case "HEBDOMADAIRE" -> "Echéancier hebdomadaire naturel " + exeCode;
            case "MENSUEL" -> "Echéancier mensuel naturel " + exeCode;
            case "TRIMESTRIEL" -> "Echéancier trimestriel naturel " + exeCode;
            case "SEMESTRIEL" -> "Echéancier semmestriel naturel " + exeCode;
            default -> "Echéancier annuel naturel " + exeCode;
        };
    }

    @Override @Transactional
    public ReadEcheancierDTO createEcheancier(CreateEcheancierDTO dto, ActionIdentifier ai)
    {
        if(dto == null) throw new AppException("Aucune donnée fournie");
        Echeancier echeancier = echeancierMapper.mapToEcheancier(dto);

        echeancier = echeancierRepo.save(echeancier);
        BeanUtils.copyProperties(ai, echeancier);
        if(dto.getEcheances() == null || dto.getEcheances().isEmpty()) return echeancierMapper.mapToReadEcheancierDTO(echeancier);
        List<ReadEcheanceDTO2> echeances = dto.getEcheances().stream().map(e->echeanceService.createEcheance(e, ai)).map(e->echeanceMapper.mapToReadEcheanceDTO2(e)).collect(Collectors.toList());
        ReadEcheancierDTO readEchancierDTO = echeancierMapper.mapToReadEcheancierDTO(echeancier);
        readEchancierDTO.setEcheances(echeances);
        return readEchancierDTO;
    }

    @Override @Transactional()
    public ReadEcheancierDTO createEcheancierNaturel(CreateEcheancierDTO dto, ActionIdentifier ai)
    {
        if(dto == null) throw new AppException("Aucune donnée fournie");
        Echeancier echeancier = echeancierRepo.findByFrequenceTypeCodeAndExeCode(dto.getFrequenceTypeCode(), dto.getExeCode());
        List<ReadEcheanceDTO2> readEcheanceDTOS = Collections.emptyList();
        if(echeancier==null)
        {
            echeancier = echeancierMapper.mapToEcheancier(dto);
            echeancier.setTypeEcheancier(new Type("ECH-NAT"));
            Exercice exe = exeRepo.findById(dto.getExeCode()).orElseThrow(() -> new AppException("Exercice introuvable " + dto.getExeCode()));
            echeancier.setExercice(exe);
            echeancier.setName(this.getNomEcheancier(dto.getFrequenceTypeCode(), dto.getExeCode().intValue()));

            echeancier = echeancierRepo.save(echeancier);
            List<LocalDate> dateEcheances = this.getDatesOfEcheancier(dto.getFrequenceTypeCode(), dto.getExeCode().intValue());
            final Long echeancierId = echeancier.getEcheancierId();
            readEcheanceDTOS = dateEcheances.stream()
                    .map(d->new CreateEcheanceDTO(d, echeancierId, dto.getFrequenceTypeCode()))
                    .map(e->echeanceService.createEcheance(e, ai))
                    .map(e->echeanceMapper.mapToReadEcheanceDTO2(e)).collect(Collectors.toList());
        }
        BeanUtils.copyProperties(ai, echeancier);
        ReadEcheancierDTO readEchancierDTO = echeancierMapper.mapToReadEcheancierDTO(echeancier);
        readEchancierDTO.setEcheances(readEcheanceDTOS);
        return readEchancierDTO;
    }

    @Override
    public ReadEcheancierDTO getEcheancier(Long echeancierId)
    {
        ReadEcheancierDTO echeancier = echeancierRepo.getEcheancier(echeancierId);
        List<ReadEcheanceDTO2> echeances = echeanceRepo.findByEcheancierId(echeancierId);
        echeancier.setEcheances(echeances);
        return echeancier;
    }

    @Override
    public ReadEcheancierDTO getEcheancierNaturel(String frequenceTypeCode, int annee)
    {
        Long echeanceId = echeancierRepo.getEcheancierNatIdByFrequenceAndExeCode(frequenceTypeCode, annee);
        return this.getEcheancier(echeanceId);
    }

    private List<LocalDate> getDatesOfEcheancierJournalier(int annee)
    {
        LocalDate debutAnnee = LocalDate.of(annee, 1, 1);
        LocalDate finAnnee = LocalDate.of(annee, 12, 31);

        return IntStream.range(0, finAnnee.getDayOfYear())
                .mapToObj(debutAnnee::plusDays)
                .collect(Collectors.toList());
    }

    private List<LocalDate> getDatesOfEcheancierHebdomadaire(int annee)
    {
            LocalDate debutAnnee = LocalDate.of(annee, 1, 1);
            return IntStream.range(0, 52)
                    .mapToObj(i -> debutAnnee.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)).plusWeeks(i))
                    .filter(date -> date.getYear() == annee)
                    .collect(Collectors.toList());
    }

    private List<LocalDate> getDatesOfEcheancierMensuel(int annee)
    {
        LocalDate firstDayOfYear = LocalDate.of(annee, 1, 1);
        return IntStream.range(0, 12).mapToObj(firstDayOfYear::plusMonths).collect(Collectors.toList());
    }

    private List<LocalDate> getDatesOfEcheancierTrimestriel(int annee)
    {
        LocalDate firstDayOfYear = LocalDate.of(annee, 1, 1);
        return IntStream.range(0, 4).mapToObj(i->firstDayOfYear.plusMonths(3*i)).collect(Collectors.toList());
    }

    private List<LocalDate> getDatesOfEcheancierSemestriel(int annee)
    {
        LocalDate firstDayOfYear = LocalDate.of(annee, 1, 1);
        return IntStream.range(0, 2).mapToObj(i->firstDayOfYear.plusMonths(6*i)).collect(Collectors.toList());
    }

    private List<LocalDate> getDatesOfEcheancierAnnuel(int annee)
    {
        LocalDate firstDayOfYear = LocalDate.of(annee, 1, 1);
        return List.of(firstDayOfYear);
    }
}