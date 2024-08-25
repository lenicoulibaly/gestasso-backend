package rigeldevsolutions.gestasso.metier.exercicemodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.exercicemodule.controller.repositories.ExerciceRepo;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.CreateExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.ReadExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.UpdateExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.entities.Exercice;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.mappers.ExerciceMapper;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.services.IEcheancierService;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.CreateEcheancierDTO;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.enums.Frequence;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ExerciceService implements IExercieService {
    private final ExerciceRepo exoRepo;
    private final ExerciceMapper exoMapper;
    private final IEcheancierService echeancierService;

    @Override @Transactional
    public ReadExerciceDTO createExercice(CreateExerciceDTO dto, ActionIdentifier ai)
    {
        Exercice exercice=exoMapper.mapToExercice(dto);
        if(dto.isExeCourant()) exoRepo.setExerciceAsNoneCourant();
        exercice=exoRepo.save(exercice);
        BeanUtils.copyProperties(ai, exercice);
        exercice=exoRepo.save(exercice);
        EnumUtils.getEnumList(Frequence.class).stream()
                .map(f->CreateEcheancierDTO.builder().typeEcheancierCode("ECH_NAT").frequence(f.name()).exeCode(dto.getExeCode()).build())
                .forEach(cedto->echeancierService.createEcheancierNaturel(cedto, ai));
        exercice=exoRepo.save(exercice);
        return exoMapper.mapToReadExerciceDTO(exercice);
    }

    @Override @Transactional
    public ReadExerciceDTO activateExercice(Long exeCode, ActionIdentifier ai)
    {
        Exercice exercice=exoRepo.findById(exeCode).orElseThrow(()->new AppException("Exercice introuvable"));
        if (exercice.isExeCourant()) return exoMapper.mapToReadExerciceDTO(exercice);

        exoRepo.setExerciceAsNoneCourant();
        exercice.setExeCourant(true);
        BeanUtils.copyProperties(ai, exercice);
        return exoMapper.mapToReadExerciceDTO(exercice);
    }

    @Override @Transactional
    public ReadExerciceDTO updateExercice(UpdateExerciceDTO dto, ActionIdentifier ai)
    {
        Exercice exercice = exoRepo.findById(dto.getExeCode()).orElseThrow(()->new AppException("Exercice introuvable"));
        exercice.setExeLibelle(dto.getExeLibelle());
        if(dto.isExeCourant()) exoRepo.setExerciceAsNoneCourant();
        exercice=exoRepo.save(exercice);
        BeanUtils.copyProperties(ai, exercice);
        return exoMapper.mapToReadExerciceDTO(exercice);
    }

    @Override
    public List<ReadExerciceDTO> searchExercice(String key) {
        return exoRepo.searchExercice(StringUtils.stripAccentsToUpperCase(key));
    }

    @Override
    public Exercice getExerciceCourant()
    {
        List<Exercice> exoCourants = exoRepo.getExeCourant();
        if(exoCourants == null || exoCourants.isEmpty()) return exoRepo.getLastExo();
        return exoCourants.get(0);
    }

    @Override
    public List<ReadExerciceDTO> getExerciceCourantAndPlus1() {
        Exercice exoCourant = this.getExerciceCourant();
        Exercice exoN1 = exoRepo.findById(exoCourant.getExeCode()+1).orElse(null);
        List<ReadExerciceDTO> exercies = Stream.of(exoCourant, exoN1).filter(Objects::nonNull)
                .map(e->exoMapper.mapToReadExerciceDTO(e))
                .collect(Collectors.toList());
        return exercies;
    }
}
