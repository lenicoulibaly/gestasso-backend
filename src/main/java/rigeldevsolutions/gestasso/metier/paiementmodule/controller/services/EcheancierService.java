package rigeldevsolutions.gestasso.metier.paiementmodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.exercicemodule.controller.repositories.ExerciceRepo;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.entities.Exercice;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.repositories.EcheancierRepo;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.*;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.entities.Echeancier;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.enums.Frequence;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.mappers.EcheanceMapper;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.mappers.EcheancierMapper;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service @RequiredArgsConstructor
public class EcheancierService implements IEcheancierService
{
    private final EcheancierRepo echeancierRepo;
    private final EcheancierMapper echeancierMapper;
    private final EcheanceMapper echeanceMapper;
    private final IEcheanceService echeanceService;
    private final ExerciceRepo exeRepo;
    @Override
    public List<LocalDate> getDatesOfEcheancier(Frequence f, int annee)
    {
        return switch (f) {
            case JOURNALIERE -> this.getDatesOfEcheancierJournalier(annee);
            case HEBDOMADAIRE -> this.getDatesOfEcheancierHebdomadaire(annee);
            case MENSUELLE -> this.getDatesOfEcheancierMensuel(annee);
            case TRIMESTRIELLE -> this.getDatesOfEcheancierTrimestriel(annee);
            case SEMESTRIELLE -> this.getDatesOfEcheancierSemestriel(annee);
            default -> this.getDatesOfEcheancierAnnuel(annee);
        };
    }

    private String getNomEcheancier(Frequence f, int exeCode)
    {
        return switch (f)
        {
            case JOURNALIERE -> "Echéancier journalier naturel " + exeCode;
            case HEBDOMADAIRE -> "Echéancier hebdomadaire naturel " + exeCode;
            case MENSUELLE -> "Echéancier mensuel naturel " + exeCode;
            case TRIMESTRIELLE -> "Echéancier trimestriel naturel " + exeCode;
            case SEMESTRIELLE -> "Echéancier semmestriel naturel " + exeCode;
            default -> "Echéancier annuel naturel " + exeCode;
        };
    }

    @Override @Transactional
    public ReadEchancierDTO createEcheancier(CreateEcheancierDTO dto, ActionIdentifier ai)
    {
        if(dto == null) throw new AppException("Aucune donnée fournie");
        Echeancier echeancier = echeancierMapper.mapToEcheancier(dto);

        echeancier = echeancierRepo.save(echeancier);
        BeanUtils.copyProperties(ai, echeancier);
        if(dto.getEcheances() == null || dto.getEcheances().isEmpty()) return echeancierMapper.mapToReadEcheancierDTO(echeancier);
        List<ReadEcheanceDTO2> echeances = dto.getEcheances().stream().map(e->echeanceService.createEcheance(e, ai)).map(e->echeanceMapper.mapToReadEcheanceDTO2(e)).collect(Collectors.toList());
        ReadEchancierDTO readEchancierDTO = echeancierMapper.mapToReadEcheancierDTO(echeancier);
        readEchancierDTO.setEcheances(echeances);
        return readEchancierDTO;
    }

    @Override @Transactional()
    public ReadEchancierDTO createEcheancierNaturel(CreateEcheancierDTO dto, ActionIdentifier ai)
    {
        if(dto == null) throw new AppException("Aucune donnée fournie");
        Echeancier echeancier = echeancierMapper.mapToEcheancier(dto);
        Exercice exe = exeRepo.findById(dto.getExeCode()).orElseThrow(()->new AppException("Aucune donnée fournie"));
        echeancier.setExercice(exe);
        echeancier = echeancierRepo.save(echeancier);

        BeanUtils.copyProperties(ai, echeancier);
        final Long echeancierId = echeancier.getEcheancierId();

        if(!EnumUtils.isValidEnum(Frequence.class, dto.getFrequence())) throw new AppException(("Fréquence inconnue"));
        Frequence frequence = EnumUtils.getEnum(Frequence.class, dto.getFrequence());
        echeancier.setName(this.getNomEcheancier(frequence, dto.getExeCode().intValue()));
        List<LocalDate> dateEcheances = this.getDatesOfEcheancier(frequence, dto.getExeCode().intValue());
        List<ReadEcheanceDTO2> readEcheanceDTOS = dateEcheances.stream()
                .map(d->new CreateEcheanceDTO(d, echeancierId))
                .map(e->echeanceService.createEcheance(e, ai))
                .map(e->echeanceMapper.mapToReadEcheanceDTO2(e)).collect(Collectors.toList());
        ReadEchancierDTO readEchancierDTO = echeancierMapper.mapToReadEcheancierDTO(echeancier);
        readEchancierDTO.setEcheances(readEcheanceDTOS);
        return readEchancierDTO;
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