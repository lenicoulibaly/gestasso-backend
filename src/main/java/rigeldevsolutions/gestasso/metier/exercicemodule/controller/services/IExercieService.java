package rigeldevsolutions.gestasso.metier.exercicemodule.controller.services;

import jakarta.transaction.Transactional;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.CreateExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.ReadExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.UpdateExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.entities.Exercice;

import java.util.List;

public interface IExercieService {
    @Transactional
    ReadExerciceDTO createExercice(CreateExerciceDTO dto) ;

    @Transactional
    ReadExerciceDTO activateExercice(Long exeCode) ;

    ReadExerciceDTO updateExercice(UpdateExerciceDTO dto) ;
    List<ReadExerciceDTO> searchExercice(String key);

    Exercice getExerciceCourant();

    List<ReadExerciceDTO> getExerciceCourantAndPlus1();
}
