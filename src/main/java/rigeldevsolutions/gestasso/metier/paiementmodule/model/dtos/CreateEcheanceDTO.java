package rigeldevsolutions.gestasso.metier.paiementmodule.model.dtos;

import lombok.*;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.validators.CoherentEcheanceDates;
import rigeldevsolutions.gestasso.metier.paiementmodule.model.validators.ExistingEcheancierId;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@CoherentEcheanceDates
public class CreateEcheanceDTO
{
    private LocalDate dateEcheance;
    private LocalDate dateButtoire;
    private BigDecimal montantEcheance;
    private BigDecimal tauxEcheance;
    @ExistingEcheancierId
    private Long echeancierId;

    public CreateEcheanceDTO(LocalDate dateEcheance, Long echeancierId) {
        this.dateEcheance = dateEcheance;
        this.echeancierId = echeancierId;
    }
}