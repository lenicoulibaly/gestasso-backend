package rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos;

import rigeldevsolutions.gestasso.metier.exercicemodule.model.validator.UniqueExoCode;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateExerciceDTO {
    @NotNull(message = "Veuillez saisir le code de l'exercice")
    @UniqueExoCode
    private Long exeCode;
    private String exeLibelle;
    private boolean exeCourant;
}
