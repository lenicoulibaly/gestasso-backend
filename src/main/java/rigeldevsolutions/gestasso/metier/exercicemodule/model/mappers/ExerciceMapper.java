package rigeldevsolutions.gestasso.metier.exercicemodule.model.mappers;

import org.mapstruct.Mapper;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.CreateExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos.ReadExerciceDTO;
import rigeldevsolutions.gestasso.metier.exercicemodule.model.entities.Exercice;

@Mapper(componentModel = "spring")
public interface ExerciceMapper {
    Exercice mapToExercice(CreateExerciceDTO dto);

    ReadExerciceDTO mapToReadExerciceDTO(Exercice exo);
}
