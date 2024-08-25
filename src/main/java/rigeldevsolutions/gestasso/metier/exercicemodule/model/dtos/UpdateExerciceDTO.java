package rigeldevsolutions.gestasso.metier.exercicemodule.model.dtos;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateExerciceDTO {
    private Long exeCode;
    private String exeLibelle;
    private boolean exeCourant;
    private String staCode;
}
