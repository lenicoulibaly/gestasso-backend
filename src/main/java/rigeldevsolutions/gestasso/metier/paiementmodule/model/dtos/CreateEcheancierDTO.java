package rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateEcheancierDTO
{
    private String typeEcheancierCode;
    private String frequence;
    private String name;
    private Long exeCode;
    List<CreateEcheanceDTO> echeances;
}
