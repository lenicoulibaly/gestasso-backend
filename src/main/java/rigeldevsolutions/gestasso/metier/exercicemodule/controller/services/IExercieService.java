package rigeldevsolutions.gestasso.metier.exercicemodule.controller.services;

import jakarta.transaction.Transactional;
import rigeldevsolutions.gestasso.authmodule.model.entities.ActionIdentifier;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.CreateExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.ReadExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.UpdateExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.entities.Exercice;

import java.util.List;

public interface IExercieService {
    @Transactional
    ReadExerciceDTO createExercice(CreateExerciceDTO dto, ActionIdentifier ai) ;

    @Transactional
    ReadExerciceDTO activateExercice(Long exeCode, ActionIdentifier ai) ;

    ReadExerciceDTO updateExercice(UpdateExerciceDTO dto, ActionIdentifier ai) ;
    List<ReadExerciceDTO> searchExercice(String key);

    Exercice getExerciceCourant();

    List<ReadExerciceDTO> getExerciceCourantAndPlus1();
}
