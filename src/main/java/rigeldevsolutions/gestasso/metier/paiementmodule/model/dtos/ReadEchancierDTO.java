package rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadEchancierDTO
{
    private String typeEcheancierCode;
    private String typeEcheancierName;
    private String frequence;
    private String name;
    private Long exeCode;
    List<ReadEcheanceDTO2> echeances;
}
