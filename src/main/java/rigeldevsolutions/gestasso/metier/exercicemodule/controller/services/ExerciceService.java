package rigeldevsolutions.gestasso.metier.exercicemodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rigeldevsolutions.gestasso.metier.exercicemodule.controller.repositories.ExerciceRepo;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.CreateExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.ReadExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.UpdateExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.entities.Exercice;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.mappers.ExerciceMapper;
import rigeldevsolutions.gestasso.metier.paiementmodule.controller.services.IEcheancierService;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos.CreateEcheancierDTO;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.StringUtils;
import rigeldevsolutions.gestasso.typemodule.controller.repositories.TypeRepo;
import rigeldevsolutions.gestasso.typemodule.model.dtos.ReadTypeDTO;
import rigeldevsolutions.gestasso.typemodule.model.enums.TypeGroup;

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
    private final TypeRepo typeRepo;

    @Override @Transactional
    public ReadExerciceDTO createExercice(CreateExerciceDTO dto)
    {
        Exercice exercice=exoMapper.mapToExercice(dto);
        if(dto.isExeCourant()) exoRepo.setExerciceAsNoneCourant();
        exercice=exoRepo.save(exercice);
        List<ReadTypeDTO> typeFrequences = typeRepo.findByTypeGroup(TypeGroup.TYPE_FREQUENCE);
        typeFrequences.stream()
                .map(f->CreateEcheancierDTO.builder().typeEcheancierCode("ECH-NAT").frequenceTypeCode(f.getUniqueCode()).exeCode(dto.getExeCode()).build())
                .forEach(cedto->echeancierService.createEcheancierNaturel(cedto));
        exercice=exoRepo.save(exercice);
        return exoMapper.mapToReadExerciceDTO(exercice);
    }

    @Override @Transactional
    public ReadExerciceDTO activateExercice(Long exeCode)
    {
        Exercice exercice=exoRepo.findById(exeCode).orElseThrow(()->new AppException("Exercice introuvable"));
        if (exercice.isExeCourant()) return exoMapper.mapToReadExerciceDTO(exercice);

        exoRepo.setExerciceAsNoneCourant();
        exercice.setExeCourant(true);
        return exoMapper.mapToReadExerciceDTO(exercice);
    }

    @Override @Transactional
    public ReadExerciceDTO updateExercice(UpdateExerciceDTO dto)
    {
        Exercice exercice = exoRepo.findById(dto.getExeCode()).orElseThrow(()->new AppException("Exercice introuvable"));
        exercice.setExeLibelle(dto.getExeLibelle());
        if(dto.isExeCourant()) exoRepo.setExerciceAsNoneCourant();
        exercice=exoRepo.save(exercice);
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
